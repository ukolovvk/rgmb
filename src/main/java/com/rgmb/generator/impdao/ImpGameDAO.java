package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.GameDAO;
import com.rgmb.generator.entity.Country;
import com.rgmb.generator.entity.Game;
import com.rgmb.generator.entity.GameCompany;
import com.rgmb.generator.entity.GameGenre;
import com.rgmb.generator.mappers.GameGenreRowMapperForFindById;
import com.rgmb.generator.mappers.GameRowMapper;
import com.rgmb.generator.mappers.GameRowMapperForFindById;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("ImpGameDAO")
@Transactional(isolation = Isolation.READ_COMMITTED)
public class ImpGameDAO implements GameDAO {
    @Autowired
    private JdbcTemplate template;

    @Autowired
    @Qualifier("ImpGameUnionCountryDAO")
    private ImpGameUnionCountryDAO gameUnionCountryDAO;

    @Autowired
    @Qualifier("ImpGameUnionGenreDAO")
    private ImpGameUnionGenreDAO gameUnionGenreDAO;

    @Autowired
    @Qualifier("CompanyDAO")
    private ImpCompanyDAO companyDAO;

    @Autowired
    @Qualifier("countryDAO")
    private ImpCountryDAO countryDAO;

    @Autowired
    @Qualifier("GameGenreDAO")
    private ImpGameGenreDAO gameGenreDAO;

    String generalSql = "WITH game_genre_table AS (SELECT games.game_id, string_agg(game_genres.genre_name,',') as genres\n" +
            "FROM games LEFT JOIN games_union_genres AS gug\n" +
            "ON games.game_id = gug.game_id\n" +
            "LEFT JOIN game_genres \n" +
            "ON gug.genre_id = game_genres.genre_id\n" +
            "GROUP BY (games.game_id)\n" +
            "), game_country_table AS (SELECT games.game_id, string_agg(countries.country_name,',') as countries\n" +
            "FROM games LEFT JOIN games_union_countries AS guc\n" +
            "ON games.game_id = guc.game_id\n" +
            "LEFT JOIN countries \n" +
            "ON guc.country_id = countries.id\n" +
            "GROUP BY (games.game_id)\n" +
            ")\n" +
            "SELECT games.game_id, games.title, ggt.genres, gct.countries, company.company_name, games.release_year, games.annotation \n" +
            "FROM games LEFT JOIN game_genre_table AS ggt\n" +
            "ON games.game_Id = ggt.game_id\n" +
            "LEFT JOIN game_country_table AS gct\n" +
            "ON games.game_id = gct.game_id\n" +
            "LEFT JOIN company \n" +
            "ON games.company_id = company.company_id";

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRED)
        public int add(Game game) {
        int companyID = companyDAO.findIdByGameCompanyName(game.getCompany().getName());
        if(companyID == 0)
            companyID = companyDAO.addWithReturningId(game.getCompany());
        String SQL = "INSERT INTO games(title, annotation,release_year,company_id) VALUES (?,?,?,?) RETURNING game_id";
        int gameID = template.queryForObject(SQL,new GameRowMapperForFindById(), game.getTitle(),game.getAnnotation(),game.getReleaseYear(),companyID);
        for(Country country : game.getCountries()){
            int countryID = countryDAO.findIdByCountryTitle(country.getName());
            if(countryID == 0)
                countryID = countryDAO.addWithReturningId(country);
            gameUnionCountryDAO.add(gameID, countryID);
        }
        for(GameGenre genre : game.getGenres()){
            int genreID = gameGenreDAO.findIdByGameGenreName(genre.getName());
            if(genreID == 0)
                genreID = gameGenreDAO.addWithReturningId(genre);
            gameUnionGenreDAO.add(gameID,genreID);
        }
        return 1;
    }


    @Override
    public int updateTitleById(int id, String title) {
        String SQL = "UPDATE games SET title = ? WHERE game_id = ?";
        return template.update(SQL, title,id);
    }

    @Override
    public int updateAnnotationById(int id, String annotation) {
        String SQL = "UPDATE games SET annotation = ? WHERE game_id = ?";
        return template.update(SQL, annotation,id);
    }

    @Override
    public int updateYearById(int id, int releaseYear) {
        String SQL = "UPDATE games SET release_year = ? WHERE game_id = ?";
        return template.update(SQL, releaseYear,id);
    }

    @Override
    public int updateCompany(int id, GameCompany company) {
        int companyID = companyDAO.findIdByGameCompanyName(company.getName());
        if(companyID == 0)
            companyID = companyDAO.addWithReturningId(company);
        String SQL = "UPDATE games SET company_id = ? WHERE game_id = ?";
        return template.update(SQL,companyID,id);
    }

    @Override
    public int updateGameGenreById(int id, List<GameGenre> gameGenres) {
        String SQL = "DELETE FROM games_union_genres WHERE game_id = ?";
        template.update(SQL,id);
        for(GameGenre genre : gameGenres){
            int genreID = gameGenreDAO.findIdByGameGenreName(genre.getName());
            if(genreID == 0)
                genreID = gameGenreDAO.addWithReturningId(genre);
            gameUnionGenreDAO.add(id,genreID);
        }
        return gameGenres.size();
    }

    @Override
    public int updateCountriesById(int id, List<Country> countries) {
        String SQL = "DELETE FROM games_union_countries WHERE game_id = ?";
        template.update(SQL,id);
        for(Country country : countries){
            int countryID = countryDAO.findIdByCountryTitle(country.getName());
            if(countryID == 0)
                countryID = countryDAO.addWithReturningId(country);
            gameUnionCountryDAO.add(id,countryID);
        }
        return countries.size();
    }

    @Override
    public List<Game> findByTitle(String title) {
        String SQL = generalSql + " WHERE games.title = ?";
        return template.query(SQL,new GameRowMapper(), title);
    }

    @Override
    public Game getRandomGame() {
        String SQL = generalSql + "  WHERE games.game_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(game_id) FROM games))) LIMIT 1";
        return template.queryForObject(SQL, new GameRowMapper());
    }

    @Override
    public Game getRandomGame(GameGenre genre) {
        String SQL = generalSql + "  WHERE games.game_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(game_id) FROM games))) AND ggt.genres ILIKE '%?%' LIMIT 1";
        return template.queryForObject(SQL, new GameRowMapper(),genre.getName());
    }

    @Override
    public Game getRandomGame(int firstYear, int secondYear) {
        String SQL = generalSql + "  WHERE games.game_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(game_id) FROM games))) AND (games.release_year BETWEEN ? AND ?) LIMIT 1";
        return template.queryForObject(SQL,new GameRowMapper(), firstYear,secondYear);
    }

    @Override
    public Game getRandomGame(GameGenre genre, int firstYear, int secondYear) {
        String SQL = generalSql + "  WHERE games.game_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(game_id) FROM games))) AND (games.release_year BETWEEN ? AND ?) AND ggt.genres ILIKE '%?%' LIMIT 1";
        return template.queryForObject(SQL,new GameRowMapper(), firstYear,secondYear,genre.getName());
    }

    @Override
    public Game findById(int id) {
        String SQL = generalSql + " WHERE books.book_id = ?";
        return template.queryForObject(SQL,new GameRowMapper(),id);
    }

    @Override
    public List<Game> findAll() {
        String SQL = generalSql;
        return template.query(SQL,new GameRowMapper());
    }

    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM games WHERE game_id = ?";
        return template.update(SQL,id);
    }
}
