package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.BookUnionGenresDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Класс реализующий все методы интерфейса BookUnionGenresDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем BookUnionGenresDAO
 */
@Repository("BookUnionGenresDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED и propagation = Propagation.REQUIRED
 */
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpBookUnionGenresDAO implements BookUnionGenresDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    JdbcTemplate template;

    /**
     * Добавление записи во вспомогательную таблицу, связывающую книги и жанры
     * @param bookID id книги
     * @param genreID id жанра
     * @return количество добавленных записей
     */
    @Override
    public int add(int bookID, int genreID) {
        String SQL = "INSERT INTO books_union_genres (book_id, genre_id) VALUES (?,?)";
        return template.update(SQL, bookID, genreID);
    }

    /**
     * Удаление записи из вспомогательной таблицы, связывающей книги и жанры
     * @param bookID id книги
     * @return количество удаленных записей
     */
    @Override
    public int delete(int bookID) {
        String SQL = "DELETE FROM books_union_genres WHERE book_id = ?";
        return template.update(SQL, bookID);
    }
}
