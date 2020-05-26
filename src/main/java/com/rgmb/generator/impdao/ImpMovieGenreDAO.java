package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.MovieGenreDAO;
import com.rgmb.generator.entity.MovieGenre;
import com.rgmb.generator.mappers.MovieGenreRowMapper;
import com.rgmb.generator.mappers.MovieGenreRowMapperForFindId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Класс реализующий все методы интерфейса MovieGenreDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем movieGenreDAO
 */
@Repository("movieGenreDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED и propagation = Propagation.REQUIRED
 * (Если у метода нет другой аннотации Transactional)
 */
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpMovieGenreDAO implements MovieGenreDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    JdbcTemplate template;

    /**
     * Метод поиска id жанра фильмов по названию
     * @param movieGenreName название жанра
     * @return id жанра
     */
    @Override
    public int findIdByMovieGenreName(String movieGenreName) {
        String SQL = "SELECT genre_id FROM movie_genres WHERE LOWER(genre_name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new MovieGenreRowMapperForFindId(), movieGenreName);
        }catch (EmptyResultDataAccessException ex){
            return 0;
        }
    }

    /**
     * Метод добавления жанра фильмов в базу данных. После добавления возвращается полученный id
     * @param movieGenre жанр {@link com.rgmb.generator.entity.MovieGenre}
     * @return полученный id жанра
     */
    @Override
    public int addWithReturningId(MovieGenre movieGenre) {
        String SQL = "INSERT INTO movie_genres(genre_name) VALUES (?) RETURNING genre_id";
        return template.queryForObject(SQL,new MovieGenreRowMapperForFindId(), movieGenre.getName());
    }

    /**
     * Поиск жанра фильмов по id
     * @param id id жанра
     * @return жанр или null,если сущность не найдена в базе даннных
     */
    @Override
    public MovieGenre findById(int id) {
        String SQL = "SELECT * FROM movie_genres WHERE genre_id = ?";
        try {
            return template.queryForObject(SQL, new MovieGenreRowMapper(), id);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Добавление жанра фильмов в базу данных
     * @param movieGenre жанр {@link com.rgmb.generator.entity.MovieGenre}
     * @return количество добавленных строк
     */
    @Override
    public int add(MovieGenre movieGenre) {
        String SQL = "INSERT INTO movie_genres(genre_name) VALUES (?)";
        return template.update(SQL,movieGenre.getName());
    }

    /**
     * Запрос всех жанров фильмов из базы данных
     * @return массив жанров {@link com.rgmb.generator.entity.MovieGenre}
     */
    @Override
    public List<MovieGenre> findAll() {
        String SQL = "SELECT * FROM movie_genres";
        try {
            return template.query(SQL, new MovieGenreRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Обновление жанра фильмов по id
     * @param id id жанра
     * @param movieGenre жанр {@link com.rgmb.generator.entity.MovieGenre}
     * @return количество обновленных строк
     */
    @Override
    public int updateById(int id, MovieGenre movieGenre) {
        String SQL = "UPDATE movie_genres SET genre_name = ? WHERE genre_id = ?";
        return template.update(SQL, movieGenre.getName(),id);
    }

    /**
     * Удаление жанра фильмов по id
     * @param id id жанра
     * @return количество удаленных строк
     */
    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM movie_genre WHERE genre_id = ?";
        return template.update(SQL,id);
    }


}
