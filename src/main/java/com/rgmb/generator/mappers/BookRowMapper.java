package com.rgmb.generator.mappers;

import com.rgmb.generator.entity.Author;
import com.rgmb.generator.entity.Book;
import com.rgmb.generator.entity.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRowMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("id");
        String title= resultSet.getString("title");
        int authorID = resultSet.getInt("author_id");
        int genreID = resultSet.getInt("genre_id");
        int size = resultSet.getInt("size");
        String annotation = resultSet.getString("annotation");
        String authorNAME = resultSet.getString("name");
        String genreNAME = resultSet.getString("genre_name");
        Author author = new Author(authorID,authorNAME);
        Genre genre = new Genre(genreID,genreNAME);
        if(annotation == null)
            return new Book(id,author,title,genre,size);
        return new Book(id,author,title,genre,size,annotation);
    }
}
