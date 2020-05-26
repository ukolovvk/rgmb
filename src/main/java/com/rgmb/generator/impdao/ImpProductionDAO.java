package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.ProductionDAO;
import com.rgmb.generator.entity.Production;
import com.rgmb.generator.mappers.ProductionRowMapper;
import com.rgmb.generator.mappers.ProductionRowMapperForFindId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Класс реализующий все методы интерфейса ProductionDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем ProductionDAO
 */
@Repository("ProductionDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED и propagation = Propagation.REQUIRED
 * (Если у метода нет другой аннотации Transactional)
 */
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpProductionDAO implements ProductionDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    JdbcTemplate template;

    /**
     * Метод поиска id кинокомпании по названию
     * @param productionName название кинокомпании
     * @return id кинокомпании или 0 в том случае,если запись не найдена
     */
    @Override
    public int findIdByProductionName(String productionName) {
        String SQL = "SELECT id FROM productions WHERE LOWER(production_name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new ProductionRowMapperForFindId(), productionName);
        }catch (EmptyResultDataAccessException ex){
            return 0;
        }
    }

    /**
     * Метод добавления кинокомпании в базу данных. После добавления возвращается полученный id
     * @param production {@link com.rgmb.generator.entity.Production}
     * @return полученный id кинокомпании
     */
    @Override
    public int addWithReturningId(Production production) {
        String SQL = "INSERT INTO productions(production_name) VALUES(?) RETURNING id";
        return template.queryForObject(SQL,new ProductionRowMapperForFindId(), production.getName());
    }

    /**
     * Поиск кинокомпании по id
     * @param id id кинокомпании
     * @return кинокомпанию или null,если сущность не найдена в базе даннных
     */
    @Override
    public Production findById(int id) {
        String SQL = "SELECT * FROM productions WHERE id = ?";
        try {
            return template.queryForObject(SQL, new ProductionRowMapper(), id);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Добавление кинокомпании в базу данных
     * @param production кинокомпания {@link com.rgmb.generator.entity.Production}
     * @return количество добавленных строк
     */
    @Override
    public int add(Production production) {
        String SQL = "INSERT INTO productions(production_name) VALUES(?)";
        return template.update(SQL,production.getName());
    }

    /**
     * Запрос всех кинокомпаний из базы данных
     * @return массив кинокомпаний {@link com.rgmb.generator.entity.Production}
     */
    @Override
    public List<Production> findAll() {
        String SQL = "SELECT * FROM productions ";
        try {
            return template.query(SQL, new ProductionRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Обновление кинокомпании по id
     * @param id id кинокомпании
     * @param production кинокомпания {@link com.rgmb.generator.entity.Production}
     * @return количество обновленных строк
     */
    @Override
    public int updateById(int id, Production production) {
        String SQL = "UPDATE productions SET production_name = ? WHERE id = ?";
        return template.update(SQL, production.getName(), id);
    }

    /**
     * Удаление кинокомпании по id
     * @param id id кинокомпании
     * @return количество удаленных строк
     */
    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM productions WHERE id = ?";
        return template.update(SQL,id);
    }
}
