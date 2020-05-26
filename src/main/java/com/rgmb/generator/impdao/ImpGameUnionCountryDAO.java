package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.GameUnionCountryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Класс реализующий все методы интерфейса GameUnionCountryDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем ImpGameUnionCountryDAO
 */
@Repository("ImpGameUnionCountryDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED и propagation = Propagation.REQUIRED
 */
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpGameUnionCountryDAO implements GameUnionCountryDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    private JdbcTemplate template;

    /**
     * Добавление записи во вспомогательную таблицу, связывающую игры и страны
     * @param gameID id игры
     * @param countryID id страны
     * @return количество добавленных записей
     */
    @Override
    public int add(int gameID, int countryID) {
        String SQL = "INSERT INTO games_union_countries(game_id,country_id) VALUES(?,?)";
        return template.update(SQL,gameID,countryID);
    }

    /**
     * Удаление записи из вспомогательной таблицы, связывающей игры и страны
     * @param gameID id игры
     * @return количество удаленных записей
     */
    @Override
    public int delete(int gameID) {
        String SQL = "DELETE FROM games_union_countries WHERE game_id = ?";
        return template.update(SQL,gameID);
    }
}
