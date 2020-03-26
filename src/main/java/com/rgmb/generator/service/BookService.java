package com.rgmb.generator.service;


import com.rgmb.generator.entity.Book;
import com.rgmb.generator.entity.Genre;
import com.rgmb.generator.exceptions.DaoException;
import com.rgmb.generator.impdao.ImpBookDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bookService")
public class BookService {

    @Autowired
    @Qualifier("bookDAO")
    private ImpBookDAO bookDao;

    public Book getRandomBook() throws DaoException{
        List<Book> arrayBook = bookDao.getRandomBook(1);
        if(arrayBook.size() != 1)
            throw new DaoException("Empty response");
        return arrayBook.get(0);
    }

    public Book getRandomBook(Genre genre) throws DaoException{
        List<Book> arrayBook = bookDao.getRandomBook(1,genre);
        if(arrayBook.size() != 1)
            throw new DaoException("Empty response");
        return arrayBook.get(0);
    }

    public Book getRandomBook(Genre genre,int min,int max) throws DaoException{
        List<Book> arrayBook = bookDao.getRandomBook(1,genre,min,max);
        if(arrayBook.size() != 1)
            throw new DaoException("Empty response");
        return arrayBook.get(0);
    }

    public Book getRandomBook(int min,int max) throws DaoException{
        List<Book> arrayBook = bookDao.getRandomBook(1,min,max);
        if(arrayBook.size() != 1)
            throw new DaoException("Empty response");
        return arrayBook.get(0);
    }

    public List<Book> getRandomBooks(int NumberOf) throws DaoException{
        List<Book> arrayBooks = bookDao.getRandomBook(NumberOf);
        if(arrayBooks.size() < NumberOf)
            throw new DaoException("Incorrect parameters");
        return arrayBooks;
    }

    public List<Book> getRandomBooks(int NumberOf,Genre genre) throws DaoException{
        List<Book> arrayBooks = bookDao.getRandomBook(NumberOf,genre);
        if(arrayBooks.size() < NumberOf)
            throw new DaoException("Incorrect parameters");
        return arrayBooks;
    }

    public List<Book> getRandomBooks(int NumberOf,Genre genre,int min,int max) throws DaoException{
        List<Book> arrayBooks = bookDao.getRandomBook(NumberOf,genre,min,max);
        if(arrayBooks.size() < NumberOf)
            throw new DaoException("Incorrect parameters");
        return arrayBooks;
    }

    public List<Book> getRandomBooks(int NumberOf,int min,int max) throws DaoException{
        List<Book> arrayBooks = bookDao.getRandomBook(NumberOf,min,max);
        if(arrayBooks.size() < NumberOf)
            throw new DaoException("Incorrect parameters");
        return arrayBooks;
    }

}
