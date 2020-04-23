package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.BookDAO;
import com.rgmb.generator.entity.Author;
import com.rgmb.generator.entity.Book;
import com.rgmb.generator.entity.Genre;
import com.rgmb.generator.mappers.BookRowMapper;
import com.rgmb.generator.mappers.BookRowMapperForFindByBookName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("bookDAO")
@Transactional(isolation = Isolation.READ_COMMITTED)
public class ImpBookDAO implements BookDAO {
    @Autowired
    private JdbcTemplate template;

    @Autowired
    @Qualifier("BookUnionGenresDAO")
    private ImpBookUnionGenresDAO bookUnionGenresDAO;

    @Autowired
    @Qualifier("BookUnionAuthorsDAO")
    private ImpBookUnionAuthorsDAO bookUnionAuthorsDAO;

    @Autowired
    @Qualifier("genreDAO")
    private ImpGenreDAO genreDAO;

    @Autowired
    @Qualifier("authorDAO")
    private ImpAuthorDAO authorDAO;

    private final String generalSql = "WITH books_genres_table AS (SELECT books.book_id, string_agg(genre.genre_name,',') as genres\n" +
            "FROM books LEFT JOIN books_union_genres AS bug\n" +
            "ON books.book_id = bug.book_id\n" +
            "LEFT JOIN genre \n" +
            "ON bug.genre_id = genre.id\n" +
            "GROUP BY (books.book_id)\n" +
            "), books_authors_table AS (SELECT books.book_id, string_agg(authors.name,',') as authors\n" +
            "FROM books LEFT JOIN books_union_authors AS bua\n" +
            "ON books.book_id = bua.book_id\n" +
            "LEFT JOIN authors \n" +
            "ON bua.author_id = authors.id\n" +
            "GROUP BY (books.book_id)\n" +
            ")\n" +
            "SELECT books.book_id, books.title, bgt.genres, bat.authors, books.rating, books.page_count, books.year, books.annotation, books.image \n" +
            "FROM books\n" +
            "LEFT JOIN books_genres_table AS bgt \n" +
            "ON books.book_id = bgt.book_id\n" +
            "LEFT JOIN books_authors_table AS bat\n" +
            "ON books.book_id = bat.book_id ";

    @Override
    public Book findById(int id) {
        String SQL = generalSql + " WHERE books.book_id = ?";
        try {
            return template.queryForObject(SQL, new BookRowMapper(), id);
        }catch(EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRED)
    public int add(Book book) {
        String SQL = "INSERT INTO books(title,page_count,rating,year, annotation, image) VALUES(?,?,?,?,?,?) RETURNING book_id";
        int bookID = template.queryForObject(SQL,new BookRowMapperForFindByBookName(), book.getTitle(),book.getSize(),book.getRating(),book.getYear(),book.getAnnotation(),book.getImageName());
        for(Genre genre : book.getGenres()){
            int genreID = genreDAO.findIdByGenreName(genre.getName());
            if(genreID == 0)
                genreID = genreDAO.addWithReturningId(genre);
            bookUnionGenresDAO.add(bookID,genreID);
        }
        for(Author author : book.getAuthors()){
            int authorID = authorDAO.findIdByAuthorName(author.getName());
            if(authorID == 0)
                authorID = authorDAO.addWithReturningId(author);
            bookUnionAuthorsDAO.add(bookID,authorID);
        }
        return 1;
    }

    @Override
    public List<Book> findAll() {
        String SQL = generalSql;
        try {
            return template.query(SQL, new BookRowMapper());
        }catch(EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM books WHERE book_id = ?";
        return template.update(SQL,id);
    }


    @Override
    public int updateAnnotationById(int id, String annotation) {
        String SQL = "UPDATE books SET annotation = ? WHERE book_id = ?";
        return template.update(SQL,annotation,id);
    }

    @Override
    public int updateSizeById(int id, int size) {
        String SQL = "UPDATE books SET page_count = ? WHERE book_id = ?";
        return template.update(SQL,size,id);
    }

    @Override
    public int updateRatingById(int id, double rating) {
        String SQL = "UPDATE books SET rating = ? WHERE book_id = ?";
        return template.update(SQL,rating,id);
    }

    @Override
    public int updateYearById(int id, int year) {
        String SQL = "UPDATE books SET year = ? WHERE book_id = ?";
        return template.update(SQL,year,id);
    }

    @Override
    public int updateTitleById(int id, String title) {
        String SQL = "UPDATE books SET title = ? WHERE book_id = ?";
        return template.update(SQL,title,id);
    }

    @Override
    public int updateAuthorsById(int id, List<Author> authors) {
        String SQL = "DELETE FROM books_union_authors WHERE book_id = ?";
        template.update(SQL,id);
        for(Author author : authors){
            int authorID = authorDAO.findIdByAuthorName(author.getName());
            if(authorID == 0)
                authorID = authorDAO.addWithReturningId(author);
            bookUnionAuthorsDAO.add(id,authorID);
        }
        return authors.size();
    }

    @Override
    public int updateGenreById(int id, List<Genre> genres) {
        String SQL = "DELETE FROM books_union_genres WHERE book_id = ?";
        template.update(SQL,id);
        for(Genre genre : genres){
            int genreID = genreDAO.findIdByGenreName(genre.getName());
            if(genreID == 0)
                genreID = genreDAO.addWithReturningId(genre);
            bookUnionGenresDAO.add(id,genreID);
        }
        return genres.size();
    }

    @Override
    public int updateImageNameById(int id, String imageName) {
        String SQL = "UPDATE books SET image = ? WHERE book_id = ?";
        return template.update(SQL,imageName,id);
    }

    @Override
    public List<Book> findByTitle(String title) {
        String SQL = generalSql + " WHERE LOWER(title) = LOWER(?)";
        try {
            return template.query(SQL, new BookRowMapper(), title);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Book getRandomBook() {
        String SQL = generalSql + "WHERE books.book_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(book_id) FROM books))) LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Book getRandomBook(Genre genre) {
        String SQL = generalSql + "WHERE books.book_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(book_id) FROM books))) AND bgt.genres ILIKE '%?%' LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper(), genre.getName());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Book getRandomBook(double FirstRating, double SecondRating) {
        String SQL = generalSql + "WHERE books.book_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(book_id) FROM books))) AND (books.rating BETWEEN ?,?)  LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper(), FirstRating, SecondRating);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
}

    @Override
    public Book getRandomBook(int minSize, int maxSize) {
        String SQL = generalSql + "WHERE books.book_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(book_id) FROM books))) AND (books.page_count BETWEEN ?,?)  LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper(), minSize, maxSize);
        }catch(EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Book getRandomBook(Genre genre, double FirstRating, double SecondRating) {
        String SQL = generalSql + "WHERE books.book_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(book_id) FROM books))) AND (books.rating BETWEEN ?,?) AND bgt.genres ILIKE '%?%'  LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper(), FirstRating, SecondRating, genre.getName());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Book getRandomBook(Genre genre, int minSize, int maxSize) {
        String SQL = generalSql + "WHERE books.book_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(book_id) FROM books))) AND (books.page_count BETWEEN ?,?) AND bgt.genres ILIKE '%?%'  LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper(), minSize, maxSize, genre.getName());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Book getRandomBook(double FirstRating, double SecondRating, int minSize, int maxSize) {
        String SQL = generalSql + "WHERE books.book_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(book_id) FROM books))) AND (books.page_count BETWEEN ?,?) AND  (books.rating BETWEEN ?,?) LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper(), minSize, maxSize, FirstRating, SecondRating);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Book getRandomBook(Genre genre, double FirstRating, double SecondRating, int minSize, int maxSize) {
        String SQL = generalSql + "WHERE books.book_id >= (SELECT ROUND(RANDOM() * (SELECT MAX(book_id) FROM books))) AND (books.page_count BETWEEN ?,?) AND  (books.rating BETWEEN ?,?) AND (bgt.genres ILIKE '%?%') LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper(), minSize, maxSize, FirstRating, SecondRating, genre.getName());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

}
