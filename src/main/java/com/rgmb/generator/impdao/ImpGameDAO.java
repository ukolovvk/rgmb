package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.GameDAO;
import com.rgmb.generator.entity.Country;
import com.rgmb.generator.entity.Game;
import com.rgmb.generator.entity.GameCompany;
import com.rgmb.generator.entity.GameGenre;
import com.rgmb.generator.mappers.GameRowMapper;
import com.rgmb.generator.mappers.GameRowMapperForFindById;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.List;


/**
 * Класс реализующий все методы интерфейса GameDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем ImpGameDAO
 */
@Repository("ImpGameDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED
 * (Если у метода нет другой аннотации Transactional)
 */
@Transactional(isolation = Isolation.READ_COMMITTED)
public class ImpGameDAO implements GameDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    private JdbcTemplate template;

    /**
     * Класс, реализующих методы обращения к вспомогательной таблице игр и стран
     * @see com.rgmb.generator.impdao.ImpGameUnionCountryDAO
     */
    @Autowired
    @Qualifier("ImpGameUnionCountryDAO")
    private ImpGameUnionCountryDAO gameUnionCountryDAO;

    /**
     * Класс, реализующих методы обращения к вспомогательной таблице игр и жанров
     * @see com.rgmb.generator.impdao.ImpGameUnionGenreDAO
     */
    @Autowired
    @Qualifier("ImpGameUnionGenreDAO")
    private ImpGameUnionGenreDAO gameUnionGenreDAO;

    /**
     * Класс, реализующих методы обращения к таблице кинокомпаний
     * @see com.rgmb.generator.impdao.ImpCompanyDAO
     */
    @Autowired
    @Qualifier("CompanyDAO")
    private ImpCompanyDAO companyDAO;

    /**
     * Класс, реализующих методы обращения к таблице стран
     * @see com.rgmb.generator.impdao.ImpCountryDAO
     */
    @Autowired
    @Qualifier("countryDAO")
    private ImpCountryDAO countryDAO;

    /**
     * Класс, реализующих методы обращения к таблице жанров игр
     * @see com.rgmb.generator.impdao.ImpGameGenreDAO
     */
    @Autowired
    @Qualifier("GameGenreDAO")
    private ImpGameGenreDAO gameGenreDAO;

    /**
     * Основной запрос, объединяющий все таблицы, связанные с играми
     */
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
            "SELECT games.game_id, games.title, ggt.genres, gct.countries, company.company_name, games.release_year, games.annotation, games.image \n" +
            "FROM games LEFT JOIN game_genre_table AS ggt\n" +
            "ON games.game_Id = ggt.game_id\n" +
            "LEFT JOIN game_country_table AS gct\n" +
            "ON games.game_id = gct.game_id\n" +
            "LEFT JOIN company \n" +
            "ON games.company_id = company.company_id";

    /**
     * Добавление игры, а также добавление соответствуюших вспомогательных сущностей в соответствующие таблицы
     * @param game игра {@link com.rgmb.generator.entity.Game}
     * @return количество добавленных игр (1 или 0)
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRED)
        public int add(Game game) {
        if(game.getCountries() == null || game.getGenres() == null)
            return -1;
        int companyID = companyDAO.findIdByGameCompanyName(game.getCompany().getName());
        if(companyID == 0)
            companyID = companyDAO.addWithReturningId(game.getCompany());
        String SQL = "INSERT INTO games(title, annotation,release_year,company_id, image) VALUES (?,?,?,?,?) RETURNING game_id";
        int gameID = template.queryForObject(SQL,new GameRowMapperForFindById(), game.getTitle(),game.getAnnotation(),game.getReleaseYear(),companyID,game.getImageName());
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

    /**
     * Обновление названия игры по id
     * @param id id игры
     * @param title название игры
     * @return количество обновленных строк
     */
    @Override
    public int updateTitleById(int id, String title) {
        String SQL = "UPDATE games SET title = ? WHERE game_id = ?";
        return template.update(SQL, title,id);
    }

    /**
     * Обновление описания игры по id
     * @param id id игры
     * @param annotation описание игры
     * @return количество обновленных строк
     */
    @Override
    public int updateAnnotationById(int id, String annotation) {
        String SQL = "UPDATE games SET annotation = ? WHERE game_id = ?";
        return template.update(SQL, annotation,id);
    }

    /**
     * Обновление года выпуска игры по id
     * @param id id игры
     * @param releaseYear год выпуска игры
     * @return количество обновленных строк
     */
    @Override
    public int updateYearById(int id, int releaseYear) {
        String SQL = "UPDATE games SET release_year = ? WHERE game_id = ?";
        return template.update(SQL, releaseYear,id);
    }

    /**
     * Обновление компании по созданию игр по id
     * @param id id игры
     * @param company компания по созданию игр
     * @return количество обновленных строк
     */
    @Override
    public int updateCompany(int id, GameCompany company) {
        int companyID = companyDAO.findIdByGameCompanyName(company.getName());
        if(companyID == 0)
            companyID = companyDAO.addWithReturningId(company);
        String SQL = "UPDATE games SET company_id = ? WHERE game_id = ?";
        return template.update(SQL,companyID,id);
    }

    /**
     * Обновление жанров игры по id
     * @param id id игры
     * @param gameGenres массив жанров игры {@link com.rgmb.generator.entity.GameGenre}
     * @return количество обновленных строк
     */
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

    /**
     * Обновление стран по id
     * @param id id игры
     * @param countries массив стран {@link com.rgmb.generator.entity.Country}
     * @return количество обновленных строк
     */
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

    /**
     * Поиск игры по названию
     * @param title название игры
     * @return массив игр на случай, если в базе найдется несколько игр с одинаковым названием
     */
    @Override
    public List<Game> findByTitle(String title) {
        String SQL = generalSql + " WHERE games.title = ?";
        try {
            return template.query(SQL, new GameRowMapper(), title);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получения рандомной игры из базы данных
     * @return игра или null в том случае, если база пуста
     */
    @Override
    public Game getRandomGame() {
        String SQL = generalSql + " ORDER BY RANDOM() LIMIT 1";
        try {
            return template.queryForObject(SQL, new GameRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получения рандомной игры из базы данных с указанным жанром
     * @param genre жанр игры {@link com.rgmb.generator.entity.GameGenre}
     * @return игра или null в том случае, если игры с таким жанром нет
     */
    @Override
    public Game getRandomGame(GameGenre genre) {
        String SQL = generalSql + "  WHERE  ggt.genres ILIKE '%" + genre.getName() + "%' ORDER BY RANDOM() LIMIT 1";
        try {
            return template.queryForObject(SQL, new GameRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получения рандомной игры из базы данных с указанным годом выпуска
     * @param firstYear левая граница отрезка
     * @param secondYear правая граница отрезка
     * @return игра или null в том случае, если игры с указанном годом выпуска нет
     */
    @Override
    public Game getRandomGame(int firstYear, int secondYear) {
        String SQL = generalSql + "  WHERE (games.release_year BETWEEN ? AND ?) ORDER BY RANDOM() LIMIT 1";
        try {
            return template.queryForObject(SQL, new GameRowMapper(), firstYear, secondYear);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получения рандомной игры из базы данных с указанным годом выпуска и жанром
     * @param genre жанр игры {@link com.rgmb.generator.entity.GameGenre}
     * @param firstYear левая граница отрезка
     * @param secondYear правая граница отрезка
     * @return игра или null в том случае, если игры с указанном годом выпуска и жанром нет
     */
    @Override
    public Game getRandomGame(GameGenre genre, int firstYear, int secondYear) {
        String SQL = generalSql + "  WHERE (games.release_year BETWEEN ? AND ?) AND ggt.genres ILIKE '%" + genre.getName() + "%' LIMIT 1";
        try {
            return template.queryForObject(SQL, new GameRowMapper(), firstYear, secondYear);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Поиск игры по id
     * @param id id игры
     * @return игра или null в том случае, если игры с таким id нет в базе данных
     */
    @Override
    public Game findById(int id) {
        String SQL = generalSql + " WHERE books.book_id = ?";
        try {
            return template.queryForObject(SQL, new GameRowMapper(), id);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение всех книг из базы данных
     * @return массив игр {@link com.rgmb.generator.entity.GameGenre}
     */
    @Override
    public List<Game> findAll() {
        String SQL = generalSql;
        try {
            return template.query(SQL, new GameRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Удаление игры по id
     * @param id id игры
     * @return количество удаленных записей
     */
    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM games WHERE game_id = ?";
        return template.update(SQL,id);
    }

    /**
     * Обновление url постера игры по id
     * @param id id игры
     * @param imageName url постера игры
     * @return количество обновленных строк
     */
    @Override
    public int updateImageNameById(int id, String imageName) {
        String SQL = "UPDATE games SET image = ? WHERE game_id = ?";
        return template.update(SQL,imageName,id);
    }

    /**
     * Получение минимального года выпуска игр из базы данных
     * @return минимальный год выпуска игр
     */
    @Override
    public int getMinYear() {
        String SQL = "SELECT MAX(release_year) AS max_year FROM games";
        return template.queryForObject(SQL, (ResultSet resultSet,int i) -> resultSet.getInt("max_year"));
    }

    /**
     * Получение максимального года выпуска игр из базы данных
     * @return максимального год выпуска игр
     */
    @Override
    public int getMaxYear() {
        String SQL = "SELECT MIN(release_year) AS min_year FROM games";
        return template.queryForObject(SQL, (ResultSet resultSet,int i) -> resultSet.getInt("min_year"));
    }
}
