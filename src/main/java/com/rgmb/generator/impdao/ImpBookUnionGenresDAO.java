package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.BookUnionGenresDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("BookUnionGenresDAO")
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpBookUnionGenresDAO implements BookUnionGenresDAO {
    @Autowired
    JdbcTemplate template;


    @Override
    public int add(int bookID, int genreID) {
        String SQL = "INSERT INTO books_union_genres (book_id, genre_id) VALUES (?,?)";
        return template.update(SQL, bookID, genreID);
    }

    @Override
    public int delete(int bookID) {
        String SQL = "DELETE FROM books_union_genres WHERE book_id = ?";
        return template.update(SQL, bookID);
    }
}
