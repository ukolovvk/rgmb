package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.BookUnionGenresDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("BookUnionAuthorsDAO")
public class ImpBookUnionAuthorsDAO implements BookUnionGenresDAO {
    @Autowired
    JdbcTemplate template;

    @Override
    public int add(int bookID, int authorID) {
        String SQL = "INSERT INTO books_union_authors(book_id,author_id) VALUES(?,?)";
        return template.update(SQL, bookID, authorID);
    }

    @Override
    public int delete(int bookID) {
        String SQL = "DELETE FROM books_union_authors WHERE book_id = ?";
        return template.update(SQL, bookID);
    }
}
