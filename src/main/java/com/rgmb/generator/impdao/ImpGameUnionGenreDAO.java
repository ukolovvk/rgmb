package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.GameUnionGenreDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Класс реализующий все методы интерфейса GameUnionGenreDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем ImpGameUnionGenreDAO
 */
@Repository("ImpGameUnionGenreDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED и propagation = Propagation.REQUIRED
 */
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpGameUnionGenreDAO implements GameUnionGenreDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    private JdbcTemplate template;

    /**
     * Добавление записи во вспомогательную таблицу, связывающую игры и жанры
     * @param gameID id игры
     * @param gameGenreID id жанра
     * @return количество добавленных записей
     */
    @Override
    public int add(int gameID, int gameGenreID) {
        String SQL = "INSERT INTO games_union_genres(game_id,genre_id) VALUES(?,?)";
        return template.update(SQL,gameID,gameGenreID);
    }

    /**
     * Удаление записи из вспомогательной таблицы, связывающей игры и жанры
     * @param gameID id игры
     * @return количество удаленных записей
     */
    @Override
    public int delete(int gameID) {
        String SQL = "DELETE FROM games_union_genres WHERE game_id = ?";
        return template.update(SQL,gameID);
    }
}
