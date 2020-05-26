package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.GenreDAO;
import com.rgmb.generator.entity.Genre;
import com.rgmb.generator.mappers.GenreRowMapper;
import com.rgmb.generator.mappers.GenreRowMapperForFindByGenreName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс реализующий все методы интерфейса CountryDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем genreDAO
 */
@Repository("genreDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED и propagation = Propagation.REQUIRED
 * (Если у метода нет другой аннотации Transactional)
 */
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpGenreDAO implements GenreDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    private JdbcTemplate template;

    /**
     * Поиск жанра книг по id
     * @param id id жанра
     * @return жанр или null,если сущность не найдена в базе даннных
     */
    @Override
    public Genre findById(int id) {
        String SQL = "SELECT * FROM genre WHERE id = ?";
        try {
            return template.queryForObject(SQL, new GenreRowMapper(), id);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Запрос всех жанров книг из базы данных
     * @return массив жанров {@link com.rgmb.generator.entity.Genre}
     */
    @Override
    public List<Genre> findAll() {
        String SQL = "SELECT * FROM genre ";
        try {
            return template.query(SQL, new GenreRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Добавление жанра книг в базу данных
     * @param genre жанр {@link com.rgmb.generator.entity.Genre}
     * @return количество добавленных строк
     */
    @Override
    public int add(Genre genre) {
        String SQL = "INSERT INTO genre (genre_name) VALUES (?) ";
        return template.update(SQL,genre.getName());
    }

    /**
     * Удаление жанра книг по id
     * @param id id жанра
     * @return количество удаленных строк
     */
    @Override
    public int deleteById(int id) {
        String SQL =  "DELETE FROM genre WHERE id = ?";
        return template.update(SQL,id);
    }

    /**
     * Обновление названия жанра книг по id
     * @param id id жанра
     * @param genreName название жанра
     * @return количество обновленных записей
     */
    @Override
    public int updateNameGenre(int id,String genreName) {
        String SQL = "UPDATE genre SET genre_name = ? WHERE id = ?";
        return template.update(SQL,genreName,id);
    }

    /**
     * Метод поиска жанра книг по названию
     * @param nameGenre название жанра
     * @return жанр или null в том случае, если запись отсутствует
     */
    @Override
    public Genre findByNameGenre(String nameGenre) {
        String SQL = "SELECT * FROM genre WHERE LOWER(genre_name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new GenreRowMapper(), nameGenre);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Метод добавления жанра книг в базу данных. После добавления возвращается полученный id
     * @param genre жанр {@link com.rgmb.generator.entity.Genre}
     * @return полученный id жанра
     */
    @Override
    public int addWithReturningId(Genre genre) {
        String SQL = "INSERT INTO genre(genre_name) VALUES(?) RETURNING id";
        return  template.queryForObject(SQL,new GenreRowMapperForFindByGenreName(), genre.getName());
    }

    /**
     * Метод поиска id жанра книнг по названию
     * @param genreName название жанра
     * @return id жанра
     */
    @Override
    public int findIdByGenreName(String genreName) {
        String SQL = "SELECT * FROM genre WHERE LOWER(genre_name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new GenreRowMapperForFindByGenreName(), genreName);
        }catch(EmptyResultDataAccessException ex){
            return 0;
        }
    }
}
