package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.MovieUnionActorsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Класс реализующий все методы интерфейса MovieUnionActorsDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем MovieUnionActorsDAO
 */
@Repository("MovieUnionActorsDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED и propagation = Propagation.REQUIRED
 */
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpMovieUnionActorsDAO implements MovieUnionActorsDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    JdbcTemplate template;

    /**
     * Добавление записи во вспомогательную таблицу, связывающую фильмы и актеров
     * @param movieID id фильма
     * @param actorID id актера
     * @return количество добавленных записей
     */
    @Override
    public int add(int movieID, int actorID) {
        String SQL = "INSERT INTO movie_union_actors(movie_id, actor_id) VALUES(?,?)";
        return template.update(SQL,movieID,actorID);
    }

    /**
     * Удаление записи из вспомогательной таблицы, связывающей фильмы и актеров
     * @param movieID id фильма
     * @return количество удаленных записей
     */
    @Override
    public int delete(int movieID) {
        String SQL = "DELETE FROM movie_union_actors WHERE movie_id = ? ";
        return template.update(SQL,movieID);
    }
}
