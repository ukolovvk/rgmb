package com.rgmb.generator.dao;


import com.rgmb.generator.entity.Book;
import com.rgmb.generator.entity.Genre;

import java.util.List;

public interface BookDAO extends GeneralBookDAO<Book>{
    int updateBookById(int id,Book book);

    int deleteByTitle(String nameBook);

    int updateAnnotationById(int id,String annotation);

    int updateSizeById(int id,int size);

    Book findByTitle(String title);

    List<Book> getRandomBook(int NumberOf);

    List<Book> getRandomBook(int NumberOf,Genre genre);

    List<Book> getRandomBook(int NumberOf,Genre genre,int minSize,int maxSize);

    List<Book> getRandomBook(int NumberOf,int minSize,int MaxSize);


}
