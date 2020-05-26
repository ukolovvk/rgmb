package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.CountryDAO;
import com.rgmb.generator.entity.Country;
import com.rgmb.generator.mappers.CountryRowMapper;
import com.rgmb.generator.mappers.CountryRowMapperForFindId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс Repository реализующий все методы интерфейса CountryDAO
 * Аннотация показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем countryDAO
 */
@Repository("countryDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED и propagation = Propagation.REQUIRED
 * (Если у метода нет другой аннотации Transactional)
 */
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpCountryDAO implements CountryDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    JdbcTemplate template;

    /**
     * Метод поиска id страны по названию
     * @param title название страны
     * @return id страны
     */
    @Override
    public int findIdByCountryTitle(String title) {
        String SQL = "SELECT id FROM countries WHERE LOWER(country_name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new CountryRowMapperForFindId(), title);
        }catch (EmptyResultDataAccessException ex){
            return 0;
        }
    }

    /**
     * Поиск страны по id
     * @param id id страны
     * @return страну или null,если сущность не найдена в базе даннных
     */
    @Override
    public Country findById(int id) {
        String SQL = "SELECT * FROM countries WHERE id = ?";
        try {
            return template.queryForObject(SQL, new CountryRowMapper(), id);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Добавление страны в базу данных
     * @param country страна {@link com.rgmb.generator.entity.Country}
     * @return количество добавленных строк
     */
    @Override
    public int add(Country country) {
        String SQL = "INSERT INTO countries(country_name) VALUES (?)";
        return template.update(SQL, country.getName());
    }

    /**
     * Метод добавления страны в базу данных. После добавления возвращается полученный id
     * @param country страна {@link com.rgmb.generator.entity.Country}
     * @return полученный id страны
     */
    @Override
    public int addWithReturningId(Country country) {
        String SQL = "INSERT INTO countries(country_name) VALUES (?) RETURNING id";
        return template.queryForObject(SQL,new CountryRowMapperForFindId(), country.getName());
    }

    /**
     * Запрос всех стран из базы данных
     * @return массив стран {@link com.rgmb.generator.entity.Country}
     */
    @Override
    public List<Country> findAll() {
        String SQL = "SELECT * FROM countries";
        try {
            return template.query(SQL, new CountryRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Обновление страны по id
     * @param id id страны
     * @param country страна {@link com.rgmb.generator.entity.Country}
     * @return количество обновленных строк
     */
    @Override
    public int updateById(int id, Country country) {
        String SQL = "UPDATE countries SET country_name = ? WHERE id = ?";
        return template.update(SQL, country.getName(), id);
    }

    /**
     * Удаление страны по id
     * @param id id страны
     * @return количество удаленных строк
     */
    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM countries WHERE id = ?";
        return template.update(SQL,id);
    }
}
