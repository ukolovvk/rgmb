package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.BookDAO;
import com.rgmb.generator.entity.Book;
import com.rgmb.generator.entity.Genre;
import com.rgmb.generator.mappers.BookRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("bookDAO")
public class ImpBookDAO implements BookDAO {
    @Autowired
    private JdbcTemplate template;

    private final String generalSQL =  "SELECT b.id, b.title, b.author_Id,b.genre_id,b.size,b.annotation," +
            "a.name,g.genre_name FROM books AS b INNER JOIN authors AS a ON b.author_id = a.id"
            + " INNER JOIN  genre AS g ON b.genre_id = g.id ";

    @Override
    public Book findById(int id) {
        String SQL = generalSQL + "WHERE b.id = ?";
        return template.queryForObject(SQL,new BookRowMapper(),id);
    }

    @Override
    public Book findByTitle(String title) {
        String SQL = generalSQL + "WHERE LOWER(title) = LOWER(?)";
        return template.queryForObject(SQL,new BookRowMapper(),title);
    }

    @Override
    public List<Book>getRandomBook(int NumberOf) {
        String SQL = generalSQL + "ORDER BY random() LIMIT ? ";
        return template.query(SQL,new BookRowMapper(),NumberOf);
    }

    @Override
    public List<Book> getRandomBook(int NumberOf,Genre genre) {
        String SQL = generalSQL + "WHERE LOWER(genre_name) = LOWER(?) ORDER BY random() LIMIT ?";
        return template.query(SQL,new BookRowMapper(),genre.getName(),NumberOf);
    }

    @Override
    public List<Book> getRandomBook(int NumberOf, Genre genre, int minSize, int maxSize) {
        String SQL = generalSQL + "WHERE LOWER(genre_name) = LOWER(?) AND (size BETWEEN ? and ?) ORDER BY random() LIMIT ?";
        return template.query(SQL,new BookRowMapper(),genre.getName(),minSize,maxSize,NumberOf);
    }

    @Override
    public List<Book> getRandomBook(int NumberOf, int minSize, int MaxSize) {
        String SQL = generalSQL + "WHERE  (size BETWEEN ? and ?) ORDER BY random() LIMIT ?";
        return template.query(SQL,new BookRowMapper(),minSize,MaxSize,NumberOf);
    }

    @Override
    public List<Book> findAll() {
        String SQL = "SELECT b.id, b.title, b.author_Id,b.genre_id,b.size,b.annotation," +
                "a.name,g.genre_name FROM books AS b INNER JOIN authors AS a ON b.author_id = a.id"
                + " INNER JOIN  genre AS g ON b.genre_id = g.id";
        return template.query(SQL,new BookRowMapper());
    }

    @Override
    public int add(Book book){
        String SQL = "INSERT INTO books (author_id,title,genre_id,size,annotation)VALUES(?,?,?,?,?)";
        return template.update(SQL,book.getAuthor().getId(),book.getTitle(),book.getGenre().getId(),book.getSize(),book.getAnnotation());
    }

    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM books WHERE id = ?";
        return template.update(SQL,id);
    }


    @Override
    public int updateBookById(int id, Book book) {
        String SQL = "UPDATE books SET author_Id = ?,title = ?,genre_id = ?,size = ?, annotation = ? WHERE id = ?";
        return template.update(SQL,book.getAuthor().getId(),book.getTitle(),book.getGenre().getId(),book.getSize(),book.getAnnotation(),id);
    }

    @Override
    public int deleteByTitle(String nameBook) {
        String SQL = "DELETE FROM books WHERE title = ?";
        return template.update(SQL,nameBook);
    }

    @Override
    public int updateAnnotationById(int id ,String annotation) {
        String SQL = "UPDATE books SET annotation = ? WHERE  id = ?";
        return template.update(SQL,annotation,id);
    }

    @Override
    public int updateSizeById(int id,int size) {
        String SQL = "UPDATE books SET size = ? WHERE id = ?";
        return template.update(SQL,size,id);
    }

}
