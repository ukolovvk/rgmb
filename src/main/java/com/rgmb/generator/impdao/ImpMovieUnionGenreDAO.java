package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.MovieUnionGenreDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Класс реализующий все методы интерфейса MovieUnionGenreDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем MovieUnionGenreDAO
 */
@Repository("MovieUnionGenreDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED и propagation = Propagation.REQUIRED
 */
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpMovieUnionGenreDAO implements MovieUnionGenreDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    JdbcTemplate template;

    /**
     * Добавление записи во вспомогательную таблицу, связывающую фильмы и жанры
     * @param movieID id фильма
     * @param movieGenreID id жанра
     * @return количество добавленных записей
     */
    @Override
    public int add(int movieID, int movieGenreID) {
        String SQL = "INSERT INTO movie_union_genres(movie_id,genre_id) VALUES(?,?)";
        return template.update(SQL,movieID,movieGenreID);
    }

    /**
     * Удаление записи из вспомогательной таблицы, связывающей фильмы и жанры
     * @param movieID id фильма
     * @return количество удаленных записей
     */
    @Override
    public int delete(int movieID) {
        String SQL = "DELETE  FROM movie_union_genres WHERE movie_id = ? ";
        return template.update(SQL,movieID);
    }
}
