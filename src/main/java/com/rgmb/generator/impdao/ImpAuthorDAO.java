package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.AuthorDAO;
import com.rgmb.generator.entity.Author;
import com.rgmb.generator.mappers.AuthorRowMapper;
import com.rgmb.generator.mappers.AuthorRowMapperForFindByAuthorName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Класс реализующий все методы интерфейса AuthorDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем authorDAO
 */
@Repository("authorDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED и propagation = Propagation.REQUIRED
 * (Если у метода нет другой аннотации Transactional)
 */
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpAuthorDAO implements AuthorDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    private JdbcTemplate template;

    /**
     * Поиск автора по id
     * @param id id автора
     * @return автора или null,если сущность не найдена в базе даннных
     */
    @Override
    public Author findById(int id) {
        String SQL = "SELECT * FROM authors WHERE id = ?";
        try {
            return template.queryForObject(SQL, new AuthorRowMapper(), id);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Поиск автора по фио
     * @param name фио автора
     * @return автора или null, если такой сущности не найдено
     */
    @Override
    public Author findByName(String name) {
        String SQL = "SELECT * FROM authors WHERE LOWER(name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new AuthorRowMapper(), name);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Метод добавления автора в базу данных. После добавления возвращается полученный id
     * @param author {@link com.rgmb.generator.entity.Author}
     * @return id автора
     */
    @Override
    public int addWithReturningId(Author author) {
        String SQL = "INSERT INTO authors(name) VALUES(?) RETURNING id";
        return template.queryForObject(SQL,new AuthorRowMapperForFindByAuthorName(),author.getName());
    }

    /**
     * Метод поиска id автора по фио
     * @param authorName фио автора
     * @return id автора
     */
    @Override
    public int findIdByAuthorName(String authorName) {
        String SQL = "SELECT * FROM authors WHERE LOWER(name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new AuthorRowMapperForFindByAuthorName(), authorName);
        }catch (EmptyResultDataAccessException ex){
            return 0;
        }
    }

    /**
     * Запрос всех авторов из базы данных
     * @return массив авторов {@link com.rgmb.generator.entity.Author}
     */
    @Override
    public List<Author> findAll() {
        String SQL = "SELECT * FROM authors";
        try {
            return template.query(SQL, new AuthorRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Добавление автора в базу данных
     * @param author автор {@link com.rgmb.generator.entity.Author}
     * @return количество добавленных строк
     */
    @Override
    public int add(Author author) {
        String SQL = "INSERT INTO authors (name) VALUES(?)";
        return template.update(SQL,author.getName());
    }

    /**
     * Удаление автора по id
     * @param id id автора
     * @return количество удаленных строк
     */
    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM authors WHERE id = ?";
        return template.update(SQL,id);
    }

    /**
     * Обновление автора по id
     * @param id id автора
     * @param name фио автора
     * @return количество обновленных строк
     */
    @Override
    public int updateNameById(int id, String name) {
        String SQL = "UPDATE authors SET name = ? WHERE id = ?";
        return template.update(SQL,name,id);
    }

}
