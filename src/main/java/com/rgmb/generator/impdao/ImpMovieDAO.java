package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.MovieDAO;
import com.rgmb.generator.entity.*;
import com.rgmb.generator.exceptions.DaoException;
import com.rgmb.generator.exceptions.IncorrectParametersDaoException;
import com.rgmb.generator.mappers.MovieRowMapper;
import com.rgmb.generator.mappers.MovieRowMapperForFindId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("MovieDAO")
@Transactional(isolation = Isolation.READ_COMMITTED)
public class ImpMovieDAO implements MovieDAO {
    @Autowired
    private JdbcTemplate template;

    @Autowired
    @Qualifier("movieGenreDAO")
    private ImpMovieGenreDAO movieGenreDAO;

    @Autowired
    @Qualifier("MovieUnionGenreDAO")
    private ImpMovieUnionGenreDAO movieUnionGenreDAO;

    @Autowired
    @Qualifier("countryDAO")
    private ImpCountryDAO countryDAO;

    @Autowired
    @Qualifier("MovieUnionCountriesDAO")
    private ImpMovieUnionCountriesDAO movieUnionCountriesDAO;

    @Autowired
    @Qualifier("actorDAO")
    private ImpActorDAO actorDAO;

    @Autowired
    @Qualifier("MovieUnionActorsDAO")
    private ImpMovieUnionActorsDAO movieUnionActorsDAO;

    @Autowired
    @Qualifier("ProductionDAO")
    private ImpProductionDAO productionDAO;

    private final String generalSelect = "WITH movie_genres_table AS (SELECT movies.movie_id, string_agg(mg.genre_name,',') as genres\n" +
            "FROM movies\n" +
            "LEFT JOIN movie_union_genres AS mug\n" +
            "ON movies.movie_id = mug.movie_id\n" +
            "LEFT JOIN movie_genres AS mg\n" +
            "ON mg.genre_id = mug.genre_id\n" +
            "GROUP BY movies.movie_id)\n" +
            "\n" +
            ", movie_actors_table AS(SELECT movies.movie_id , string_agg(actors.actor_name,',') as actors\n" +
            "FROM movies\n" +
            "LEFT JOIN movie_union_actors AS mua\n" +
            "ON movies.movie_id = mua.movie_id\n" +
            "LEFT JOIN actors\n" +
            "ON actors.actor_id = mua.actor_id\n" +
            "GROUP BY movies.movie_id)\n" +
            "\n" +
            ", movie_countries_table AS(SELECT movies.movie_id, string_agg(countries.country_name,',') as countries\n" +
            "FROM movies\n" +
            "LEFT JOIN movie_union_countries as muc\n" +
            "ON movies.movie_id = muc.movie_id\n" +
            "LEFT JOIN countries\n" +
            "ON muc.country_id = countries.id\n" +
            "GROUP BY movies.movie_id)\n" +
            "\n" +
            "SELECT movies.movie_id, movies.movie_title, prod.production_name,mgt.genres, mat.actors, mct.countries,\n" +
            "movies.rating, movies.annotation, movies.runtime, movies.image, movies.release_date FROM movies  \n" +
            "LEFT JOIN movie_actors_table AS mat \n" +
            "ON movies.movie_id = mat.movie_id\n" +
            "LEFT JOIN movie_countries_table as mct\n" +
            "ON movies.movie_id = mct.movie_id\n" +
            "LEFT JOIN movie_genres_table as mgt\n" +
            "ON movies.movie_id = mgt.movie_id\n" +
            "LEFT JOIN productions as prod\n" +
            "ON movies.production_id = prod.id ";

    @Override
    public Movie findById(int id) {
        String SQL = generalSelect + " WHERE movies.movie_id = ?";
        try {
            return template.queryForObject(SQL, new MovieRowMapper(), id);
        }catch(EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRED)
    public int add(Movie movie) {
        int productionID = productionDAO.findIdByProductionName(movie.getProduction().getName());
        if(productionID == 0)
            productionID = productionDAO.addWithReturningId(movie.getProduction());
        String SQL = "INSERT INTO movies(movie_title, production_id, rating, annotation, runtime, image, release_date) VALUES (?,?,?,?,?,?,?) RETURNING movie_id";
        int movieID = template.queryForObject(SQL, new MovieRowMapperForFindId(),movie.getTitle(), productionID, movie.getRating(), movie.getAnnotation(), movie.getRuntime(), movie.getUrlImage(), movie.getReleaseDate());
        for(MovieGenre genre : movie.getGenreList()){
            int localID = movieGenreDAO.findIdByMovieGenreName(genre.getName());
            if(localID == 0)
                localID = movieGenreDAO.addWithReturningId(genre);
            movieUnionGenreDAO.add(movieID,localID);
        }
        for(Country country : movie.getCountryList()){
            int localID = countryDAO.findIdByCountryTitle(country.getName());
            if(localID == 0)
                localID = countryDAO.addWithReturningId(country);
            movieUnionCountriesDAO.add(movieID,localID);
        }
        for(Actor actor : movie.getListActors()){
            int localID = actorDAO.findIdByActorName(actor.getName());
            if(localID == 0)
                localID = actorDAO.addWithReturningId(actor);
            movieUnionActorsDAO.add(movieID,localID);
        }
        return 1;
    }

    @Override
    public List<Movie> findAll() {
        String SQL = generalSelect;
        try {
            return template.query(SQL, new MovieRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public int updateById(int id, Movie movie) {
        int productionID = productionDAO.findIdByProductionName(movie.getProduction().getName());
        if(productionID == 0)
            productionID =  productionDAO.addWithReturningId(movie.getProduction());
        String SQL = "UPDATE movies SET movie_title = ?, production_id = ?, rating = ?, annotation = ?, runtime = ?, image = ?, release_date = ? WHERE movie_id = ?";
        return template.update(movie.getTitle(), productionID, movie.getRating(), movie.getAnnotation(), movie.getRuntime(), movie.getUrlImage(), movie.getReleaseDate(), id);
    }

    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM movies WHERE movie_id = ?";
        return template.update(SQL,id);
    }

    @Override
    public Movie getRandomMovie() {
        String SQL = generalSelect + " WHERE movies.movie_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(movie_id) FROM movies))) LIMIT 1 ";
        try {
            return template.queryForObject(SQL, new MovieRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public List<Movie> getRandomMovies(int numberOfMovies) {
        String SQL = generalSelect + " WHERE movies.movie_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(movie_id) FROM movies))) LIMIT ?";
        try {
            return template.query(SQL, new MovieRowMapper(), numberOfMovies);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Movie getRandomMovie(MovieGenre genre) throws DaoException{
        String SQL = generalSelect + " WHERE movies.movie_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(movie_id) FROM movies))) AND mgt.genres ILIKE '%?%' LIMIT 1 ";
        try {
            return template.queryForObject(SQL, new MovieRowMapper(), genre.getName());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Movie getRandomMovie(int firstYear, int secondYear) throws DaoException{
        String SQL = generalSelect + " WHERE movies.movie_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(movie_id) FROM movies))) AND (movies.release_date BETWEEN ? AND ?)  LIMIT 1";
        try {
            return template.queryForObject(SQL, new MovieRowMapper(), firstYear, secondYear);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Movie getRandomMovie(MovieGenre genre, int firstYear, int secondYear) throws DaoException{
        String SQL = generalSelect + " WHERE movies.movie_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(movie_id) FROM movies))) AND (movies.release_date BETWEEN ? AND ?) AND mgt.genres ILIKE '%?%'  LIMIT 1";
        try {
            return template.queryForObject(SQL, new MovieRowMapper(), firstYear, secondYear, genre.getName());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Movie getRandomMovie(Production production) throws DaoException{
        String SQL = generalSelect + "  WHERE movies.movie_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(movie_id) FROM movies))) AND prod.production_name ILIKE '%?%' LIMIT 1 ";
        try {
            return template.queryForObject(SQL, new MovieRowMapper(), production.getName());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Movie getRandomMovie(MovieGenre genre, Production production) throws DaoException{
        String SQL = generalSelect + " WHERE movies.movie_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(movie_id) FROM movies))) AND prod.production_name ILIKE '%?%' AND  mgt.genres ILIKE '%?%' LIMIT 1";
        try {
            return template.queryForObject(SQL, new MovieRowMapper(), production.getName(), genre.getName());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Movie getRandomMovie(int firstYear, int secondYear, Production production) throws DaoException{
        String SQL = generalSelect + " WHERE movies.movie_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(movie_id) FROM movies))) AND (movies.release_date BETWEEN ? AND ?) AND prod.production_name ILIKE '%?%' LIMIT 1";
        try {
            return template.queryForObject(SQL, new MovieRowMapper(), firstYear, secondYear,production.getName());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Movie getRandomMovie(MovieGenre genre, int firstYear, int secondYear, Production production) throws DaoException{
        String SQL = generalSelect + " WHERE movies.movie_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(movie_id) FROM movies))) AND (movies.release_date BETWEEN ? AND ?) AND prod.production_name ILIKE '%?%' AND mgt.genres ILIKE '%?%' LIMIT 1";
        try {
            return template.queryForObject(SQL, new MovieRowMapper(), firstYear, secondYear, production.getName(), genre.getName());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public int updateTitleById(int id, String title) {
        String SQL = "UPDATE movies SET title = ? WHERE movie_id = ?";
        return template.update(SQL,title,id);
    }

    @Override
    public int updateRatingById(int id, double rating) throws IncorrectParametersDaoException{
        if(rating < 0 || rating > 10)
            throw new IncorrectParametersDaoException("Incorrect parameters ... ");
        String SQL = "UPDATE movies SET title = ? WHERE movie_id = ?";
        return template.update(SQL,rating,id);
    }

    @Override
    public int updateRuntimeById(int id, int runtime) throws IncorrectParametersDaoException{
        if(runtime < 0 || runtime > 1000)
            throw new IncorrectParametersDaoException("Incorrect parameters...");
        String SQL = "UPDATE movies SET runtime = ? WHERE movie_id = ?";
        return template.update(SQL,runtime,id);
    }

    @Override
    public int updateDateReleaseById(int id, int year) throws IncorrectParametersDaoException{
        if(year < 0)
            throw new IncorrectParametersDaoException("Incorrect parameters...");
        String SQL = "UPDATE movies SET release_date = ? WHERE movie_id = ?";
        return template.update(SQL,year,id);
    }

    @Override
    public int updateProductionById(int id, Production production) {
        int productionID = productionDAO.findIdByProductionName(production.getName());
        if(productionID == 0)
            productionID = productionDAO.addWithReturningId(production);
        String SQL = "UPDATE movies SET production_id = ? WHERE movie_id = ?";
        return template.update(SQL,productionID,id);
    }

    @Override
    public int updateAnnotationById(int id, String annotation) {
        String SQL = "UPDATE movies SET annotation = ? WHERE movie_id = ?";
        return template.update(SQL,annotation,id);
    }

    @Override
    public int updateImageURLById(int id, String url) {
        String SQL = "UPDATE movies SET image = ? WHERE movie_id = ?";
        return template.update(SQL,url,id);
    }

    @Override
    public int updateGenreById(int id, List<MovieGenre> genres) {
        String SQLForDelete = "DELETE FROM movie_union_genres WHERE movie_id = ?";
        template.update(SQLForDelete, id);
        for(MovieGenre genre : genres){
            int genreID = movieGenreDAO.findIdByMovieGenreName(genre.getName());
            if(genreID == 0)
                genreID = movieGenreDAO.addWithReturningId(genre);
            movieUnionGenreDAO.add(id,genreID);
        }
        return genres.size();
    }

    @Override
    public int updateActorsById(int id, List<Actor> actors) {
        String SQLForDelete = "DELETE FROM movie_union_actors WHERE movie_id = ?";
        template.update(SQLForDelete,id);
        for(Actor actor : actors){
            int actorID = actorDAO.findIdByActorName(actor.getName());
            if(actorID == 0)
                actorID = actorDAO.addWithReturningId(actor);
            movieUnionActorsDAO.add(id,actorID);
        }
        return actors.size();
    }

    @Override
    public int updateCountriesById(int id, List<Country> countries) {
        String SQLForDelete = "DELETE FROM movie_union_countries WHERE movie_id = ?";
        template.update(SQLForDelete,id);
        for(Country country : countries){
            int countryID = countryDAO.findIdByCountryTitle(country.getName());
            if(countryID == 0)
                countryID = countryDAO.addWithReturningId(country);
            movieUnionCountriesDAO.add(id,countryID);
        }
        return countries.size();
    }


}
