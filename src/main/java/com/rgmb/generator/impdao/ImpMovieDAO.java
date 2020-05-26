package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.MovieDAO;
import com.rgmb.generator.entity.*;
import com.rgmb.generator.mappers.MovieRowMapper;
import com.rgmb.generator.mappers.MovieRowMapperForFindId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Класс реализующий все методы интерфейса MovieDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем MovieDAO
 */
@Repository("MovieDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED
 * (Если у метода нет другой аннотации Transactional)
 */
@Transactional(isolation = Isolation.READ_COMMITTED)
public class ImpMovieDAO implements MovieDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    private JdbcTemplate template;

    /**
     * Класс, реализующих методы обращения к таблице жанров фильмов
     * @see com.rgmb.generator.impdao.ImpMovieGenreDAO
     */
    @Autowired
    @Qualifier("movieGenreDAO")
    private ImpMovieGenreDAO movieGenreDAO;

    /**
     * Класс, реализующих методы обращения к вспомогательной таблице фильмов и жанров
     * @see com.rgmb.generator.impdao.ImpMovieUnionGenreDAO
     */
    @Autowired
    @Qualifier("MovieUnionGenreDAO")
    private ImpMovieUnionGenreDAO movieUnionGenreDAO;

    /**
     * Класс, реализующих методы обращения к таблице стран
     * @see com.rgmb.generator.impdao.ImpCountryDAO
     */
    @Autowired
    @Qualifier("countryDAO")
    private ImpCountryDAO countryDAO;

    /**
     * Класс, реализующих методы обращения к вспомогательной таблице фильмов и стран
     * @see com.rgmb.generator.impdao.ImpMovieUnionCountriesDAO
     */
    @Autowired
    @Qualifier("MovieUnionCountriesDAO")
    private ImpMovieUnionCountriesDAO movieUnionCountriesDAO;

    /**
     * Класс, реализующих методы обращения к таблице стран
     * @see com.rgmb.generator.impdao.ImpActorDAO
     */
    @Autowired
    @Qualifier("actorDAO")
    private ImpActorDAO actorDAO;

    /**
     * Класс, реализующих методы обращения к вспомогательной таблице фильмов и актеров
     * @see com.rgmb.generator.impdao.ImpMovieUnionActorsDAO
     */
    @Autowired
    @Qualifier("MovieUnionActorsDAO")
    private ImpMovieUnionActorsDAO movieUnionActorsDAO;

    /**
     * Класс, реализующих методы обращения к таблице стран
     * @see com.rgmb.generator.impdao.ImpProductionDAO
     */
    @Autowired
    @Qualifier("ProductionDAO")
    private ImpProductionDAO productionDAO;

    /**
     * Основной запрос, объединяющий все таблицы, связанные с фильмами
     */
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

    /**
     * Получение фильма по id
     * @param id id фильма
     * @return фильм или null в том случае, если фильма с таким id в базе данных не найдено
     */
    @Override
    public Movie findById(int id) {
        String SQL = generalSelect + " WHERE movies.movie_id = ?";
        try {
            return template.queryForObject(SQL, new MovieRowMapper(), id);
        }catch(EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Добавление фильма, а также добавление соответствуюших вспомогательных сущностей в соответствующие таблицы
     * @param movie фильм {@link com.rgmb.generator.entity.Movie}
     * @return количество добавленных записей
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRED)
    public int add(Movie movie) {
        if(movie.getGenreList() == null || movie.getCountryList() == null || movie.getListActors() == null)
            return 0;
        int productionID = productionDAO.findIdByProductionName(movie.getProduction().getName());
        if(productionID == 0)
            productionID = productionDAO.addWithReturningId(movie.getProduction());
        String SQL = "INSERT INTO movies(movie_title, production_id, rating, annotation, runtime, image, release_date) VALUES (?,?,?,?,?,?,?) RETURNING movie_id";
        int movieID = template.queryForObject(SQL, new MovieRowMapperForFindId(),  movie.getTitle(), productionID, movie.getRating(), movie.getAnnotation(), movie.getRuntime(), movie.getUrlImage(), movie.getReleaseDate());

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

    /**
     * Получение всех фильмов из базы данных
     * @return массив фильмов или null, если null, если в таблице нет записей
     */
    @Override
    public List<Movie> findAll() {
        String SQL = generalSelect;
        try {
            return template.query(SQL, new MovieRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Обновление фильма по id
     * @param id id фильма
     * @param movie фильм {@link com.rgmb.generator.entity.Movie}
     * @return количество обновленных записей
     */
    @Override
    public int updateById(int id, Movie movie) {
        int productionID = productionDAO.findIdByProductionName(movie.getProduction().getName());
        if(productionID == 0)
            productionID =  productionDAO.addWithReturningId(movie.getProduction());
        String SQL = "UPDATE movies SET movie_title = ?, production_id = ?, rating = ?, annotation = ?, runtime = ?, image = ?, release_date = ? WHERE movie_id = ?";
        return template.update(movie.getTitle(), productionID, movie.getRating(), movie.getAnnotation(), movie.getRuntime(), movie.getUrlImage(), movie.getReleaseDate(), id);
    }

    /**
     * Удаление фильма по id
     * @param id id фильма
     * @return количество удаленных строк
     */
    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM movies WHERE movie_id = ?";
        return template.update(SQL,id);
    }

    /**
     * Получение рандомного фильма из базы данных
     * @return фильм или null, если таблица фильмов пуста
     */
    @Override
    public Movie getRandomMovie() {
        String SQL = generalSelect + " ORDER BY RANDOM() LIMIT 1 ";
        try {
            return template.queryForObject(SQL, new MovieRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение рандомного фильма с указанным  жанром из базы данных
     * @param genre жанр {@link com.rgmb.generator.entity.MovieGenre}
     * @return фильм или null, если фильма с указанным жанром нет в таблице
     */
    @Override
    public Movie getRandomMovie(MovieGenre genre){
        String SQL = generalSelect + " WHERE mgt.genres ILIKE '%" + genre.getName() + "%' ORDER BY RANDOM() LIMIT 1";
        try {
            return template.queryForObject(SQL, new MovieRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение рандомного фильма с указанным годом выпуска из базы данных
     * @param firstYear левая граница отрезка
     * @param secondYear правая граница отрезка
     * @return фильм или null, если фильма с указанным годом выпуска нет в таблице
     */
    @Override
    public Movie getRandomMovie(int firstYear, int secondYear) {
        String SQL = generalSelect + " WHERE movies.release_date BETWEEN ? AND ?  ORDER BY RANDOM()  LIMIT 1";
        try {
            return template.queryForObject(SQL, new MovieRowMapper(), firstYear, secondYear);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение рандомного фильма с указанными жанром и  годом выпуска из базы данных
     * @param genre жанр {@link com.rgmb.generator.entity.MovieGenre}
     * @param firstYear левая граница отрезка
     * @param secondYear правая граница отрезка
     * @return фильм или null, если фильма с указанными жанром и годом выпуска нет в таблице
     */
    @Override
    public Movie getRandomMovie(MovieGenre genre, int firstYear, int secondYear) {
        String SQL = generalSelect + " WHERE  (movies.release_date BETWEEN ? AND ?) AND mgt.genres ILIKE '%" + genre.getName() +  "%'  ORDER BY RANDOM()  LIMIT 1";
        try {
            return template.queryForObject(SQL, new MovieRowMapper(), firstYear, secondYear);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение рандомного фильма с указанной  кинокомпанией
     * @param production кинокомпания {@link com.rgmb.generator.entity.Production}
     * @return фильм или null, если фильма с указанной кинокомпанией нет в таблице
     */
    @Override
    public Movie getRandomMovie(Production production) {
        String SQL = generalSelect + "  WHERE prod.production_name ILIKE '%" + production.getName() +  "%' ORDER BY RANDOM()  LIMIT 1 ";
        try {
            return template.queryForObject(SQL, new MovieRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение рандомного фильма с указанными жанром и кинокомпанией
     * @param genre жанр {@link com.rgmb.generator.entity.MovieGenre}
     * @param production кинокомпания {@link com.rgmb.generator.entity.Production}
     * @return фильм или null, если фильма с указанными кинокомпанией и жанром нет в таблице
     */
    @Override
    public Movie getRandomMovie(MovieGenre genre, Production production){
        String SQL = generalSelect + " WHERE  prod.production_name ILIKE '%" + production.getName() + "%' AND  mgt.genres ILIKE '%" + genre.getName() + "%' ORDER BY RANDOM()  LIMIT 1";
        try {
            return template.queryForObject(SQL, new MovieRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение рандомного фильма с указанными годами выпуска и кинокомпанией
     * @param firstYear левая граница отрезка
     * @param secondYear правая граница отрезка
     * @param production кинокомпания {@link com.rgmb.generator.entity.Production}
     * @return фильм или null, если фильма с указанными годами выпуска и кинокомпанией нет в таблице
     */
    @Override
    public Movie getRandomMovie(int firstYear, int secondYear, Production production) {
        String SQL = generalSelect + " WHERE  (movies.release_date BETWEEN ? AND ?) AND prod.production_name ILIKE '%" + production.getName() + "%' LIMIT 1";
        try {
            return template.queryForObject(SQL, new MovieRowMapper(), firstYear, secondYear);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение рандомного фильма с указанными жанром, годами выпуска и кинокомпанией
     * @param genre жанр {@link com.rgmb.generator.entity.MovieGenre}
     * @param firstYear левая граница отрезка
     * @param secondYear правая граница отрезка
     * @param production кинокомпания {@link com.rgmb.generator.entity.Production}
     * @return фильм или null, если фильма с указанными годами выпуска, кинокомпанией и жанром нет в таблице
     */
    @Override
    public Movie getRandomMovie(MovieGenre genre, int firstYear, int secondYear, Production production){
        String SQL = generalSelect + " WHERE (movies.release_date BETWEEN ? AND ?) AND prod.production_name ILIKE '%" + production.getName() + "%' AND mgt.genres ILIKE '%" + genre.getName() + "%' LIMIT 1";
        try {
            return template.queryForObject(SQL, new MovieRowMapper(), firstYear, secondYear);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение максимального года выпуска фильмов из базы данных
     * @return максимальный год выпуска
     */
    @Override
    public int getMaxYear() {
        String SQL = "SELECT MAX(movies.release_date) AS max_year FROM movies";
        return template.queryForObject(SQL,(ResultSet resultSet,int i) -> resultSet.getInt("max_year"));
    }

    /**
     * Получение минимального года выпуска фильмов из базы данных
     * @return минимальный год выпуска
     */
    @Override
    public int getMinYear() {
        String SQL = "SELECT MIN(movies.release_date) AS min_year FROM movies";
        return template.queryForObject(SQL,(ResultSet resultSet, int i) -> resultSet.getInt("min_year"));
    }

    /**
     * Обновление названия фильма по id
     * @param id id фильма
     * @param title название фильма
     * @return количество обновленных строк
     */
    @Override
    public int updateTitleById(int id, String title) {
        String SQL = "UPDATE movies SET title = ? WHERE movie_id = ?";
        return template.update(SQL,title,id);
    }

    /**
     * Обновление рейтинга фильма по id
     * @param id id фильма
     * @param rating рейтинг фильма
     * @return количество обновленных строк
     */
    @Override
    public int updateRatingById(int id, double rating) {
        String SQL = "UPDATE movies SET title = ? WHERE movie_id = ?";
        return template.update(SQL,rating,id);
    }

    /**
     * Обновление длительности фильма по id
     * @param id id фильма
     * @param runtime длительность фильма
     * @return количество обновленных строк
     */
    @Override
    public int updateRuntimeById(int id, int runtime){
        String SQL = "UPDATE movies SET runtime = ? WHERE movie_id = ?";
        return template.update(SQL,runtime,id);
    }

    /**
     * Обновление года выпуска фильма по id
     * @param id id фильма
     * @param year год выпуска
     * @return количество обновленных строк
     */
    @Override
    public int updateDateReleaseById(int id, int year){
        String SQL = "UPDATE movies SET release_date = ? WHERE movie_id = ?";
        return template.update(SQL,year,id);
    }

    /**
     * Обновление кинокомпании по id
     * @param id id фильма
     * @param production кинокомпания
     * @return количество обновленных строк
     */
    @Override
    public int updateProductionById(int id, Production production) {
        int productionID = productionDAO.findIdByProductionName(production.getName());
        if(productionID == 0)
            productionID = productionDAO.addWithReturningId(production);
        String SQL = "UPDATE movies SET production_id = ? WHERE movie_id = ?";
        return template.update(SQL,productionID,id);
    }

    /**
     * Обновление описания фильма по id
     * @param id id фильма
     * @param annotation описание фильма
     * @return количество обновленных строк
     */
    @Override
    public int updateAnnotationById(int id, String annotation) {
        String SQL = "UPDATE movies SET annotation = ? WHERE movie_id = ?";
        return template.update(SQL,annotation,id);
    }

    /**
     * Обновление url постера фильма по id
     * @param id id фильма
     * @param url url постера
     * @return количество обновленных строк
     */
    @Override
    public int updateImageURLById(int id, String url) {
        String SQL = "UPDATE movies SET image = ? WHERE movie_id = ?";
        return template.update(SQL,url,id);
    }

    /**
     * Обновление жанров фильма по id
     * @param id id фильма
     * @param genres массив жанров {@link com.rgmb.generator.entity.MovieGenre}
     * @return количество обновленных строк
     */
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

    /**
     * Обновление актеров фильма по id
     * @param id id фильма
     * @param actors массив актеров {@link com.rgmb.generator.entity.Actor}
     * @return количество обновленных строк
     */
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

    /**
     * Обновление стран фильма по id
     * @param id id фильма
     * @param countries массив стран {@link com.rgmb.generator.entity.Country}
     * @return количество обновленных строк
     */
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
