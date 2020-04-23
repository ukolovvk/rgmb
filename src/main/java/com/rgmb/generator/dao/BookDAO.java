package com.rgmb.generator.dao;


import com.rgmb.generator.entity.Author;
import com.rgmb.generator.entity.Book;
import com.rgmb.generator.entity.Genre;

import java.util.List;

public interface BookDAO extends GeneralBookDAO<Book>{
    int updateAnnotationById(int id,String annotation);

    int updateSizeById(int id,int size);

    int updateRatingById(int id, double rating);

    int updateYearById(int id, int year);

    int updateTitleById(int id, String title);

    int updateAuthorsById(int id, List<Author> authors);

    int updateGenreById(int id, List<Genre> genres);

    int updateImageNameById(int  id, String imageName);

    List<Book> findByTitle(String title);

    Book getRandomBook();

    Book getRandomBook(Genre genre);

    Book getRandomBook(double FirstRating, double SecondRating);

    Book getRandomBook(int minSize, int maxSize);

    Book getRandomBook(Genre genre, double FirstRating, double SecondRating);

    Book getRandomBook(Genre genre, int minSize, int maxSize);

    Book getRandomBook(double FirstRating, double SecondRating,int minSize, int maxSize);

    Book getRandomBook(Genre genre, double FirstRating, double SecondRating,int minSize, int maxSize);


}
