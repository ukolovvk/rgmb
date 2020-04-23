package com.rgmb.generator.mappers;

import com.rgmb.generator.entity.Author;
import com.rgmb.generator.entity.Book;
import com.rgmb.generator.entity.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRowMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("book_id");
        String title = resultSet.getString("title");
        String  resultGenres = resultSet.getString("genres");
        List<Genre> resultGenresList = new ArrayList<>();
        if(resultGenres != null) {
            String genres[] = resultGenres.split(",");
            for (String genre : genres) {
                resultGenresList.add(new Genre(genre));
            }
        }
        String resultAuthors = resultSet.getString("authors");
        List<Author> resultAuthorsList = new ArrayList<>();
        if(resultAuthors != null) {
            String authors[] = resultAuthors.split(",");
            for (String author : authors) {
                resultAuthorsList.add(new Author(author));
            }
        }
        double rating = resultSet.getDouble("rating");
        int pageCount = resultSet.getInt("page_count");
        int year = resultSet.getInt("year");
        String annotation = resultSet.getString("annotation");
        if(annotation == null)
            annotation = "";
        String imageName = resultSet.getString("image");
        if(imageName == null)
            imageName = "";
        if(year == 0)
            return new Book(id,resultAuthorsList,title,resultGenresList,pageCount,annotation,rating,imageName);

        return new Book(id,resultAuthorsList,title,resultGenresList,pageCount,annotation,rating,year,imageName);
    }
}
