package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.CompanyDAO;
import com.rgmb.generator.entity.GameCompany;
import com.rgmb.generator.mappers.CompanyRowMapperForFindById;
import com.rgmb.generator.mappers.GameCompanyRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Класс реализующий все методы интерфейса CompanyDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем CompanyDAO
 */
@Repository("CompanyDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED и propagation = Propagation.REQUIRED
 * (Если у метода нет другой аннотации Transactional)
 */
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpCompanyDAO implements CompanyDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    JdbcTemplate template;

    /**
     * Метод поиска id компании по созданию игр по названию
     * @param gameCompanyName название компании по созданию игр
     * @return id компании по созданию игр
     */
    @Override
    public int findIdByGameCompanyName(String gameCompanyName) {
        String SQL = "SELECT company_id FROM company WHERE LOWER(company_name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new CompanyRowMapperForFindById(), gameCompanyName);
        }catch (EmptyResultDataAccessException ex){
            return 0;
        }
    }

    /**
     * Метод добавления компании в базу данных. После добавления возвращается полученный id
     * @param company {@link com.rgmb.generator.entity.GameCompany}
     * @return полученный id компании по созданию игр
     */
    @Override
    public int addWithReturningId(GameCompany company) {
        String SQL = "INSERT INTO company(company_name) VALUES (?) RETURNING company_id";
        return template.queryForObject(SQL,new CompanyRowMapperForFindById(),company.getName());
    }

    /**
     * Поиск компании по созданию игр по id
     * @param id id компании по созданию игр
     * @return компанию по созданию игр или null,если сущность не найдена в базе даннных
     */
    @Override
    public GameCompany findById(int id) {
        String SQL = "SELECT * FROM company WHERE company_id = ?";
        try {
            return template.queryForObject(SQL, new GameCompanyRowMapper(), id);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Запрос всех компаний по созданию игр из базы данных
     * @return массив компаний по созданию игр {@link com.rgmb.generator.entity.GameCompany}
     */
    @Override
    public List<GameCompany> findAll() {
        String SQL = "SELECT * FROM company";
        try {
            return template.query(SQL, new GameCompanyRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Добавление компании по созданию игр в базу данных
     * @param company компания по созданию игр {@link com.rgmb.generator.entity.GameCompany}
     * @return количество добавленных строк
     */
    @Override
    public int add(GameCompany company) {
        String SQL = "INSERT INTO company(company_name) VALUES(?)";
        return template.update(SQL,company.getName());
    }

    /**
     * Обновление компании по созданию игр по id
     * @param id id компании по созданию игр
     * @param gameCompany компания по созданию игр {@link com.rgmb.generator.entity.GameCompany}
     * @return количество обновленных строк
     */
    @Override
    public int updateById(int id, GameCompany gameCompany) {
        String SQL = "UPDATE company SET company_name = ? WHERE company_id = ?";
        return template.update(SQL, gameCompany.getName(),id);
    }

    /**
     * Удаление компании по созданию игр по id
     * @param id id компании по созданию игр
     * @return количество удаленных строк
     */
    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM company WHERE company_id = ?";
        return template.update(SQL,id);
    }
}
