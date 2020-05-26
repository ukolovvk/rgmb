package com.rgmb.generator.entity;
import java.util.List;

/**
 * Класс сущности книга
 * Соответствующая таблица в базе данных - books
 */
public class Book {
    /**
     * id книги в базе данных
     */
    private int id;
    /**
     * Список авторов, написавших книгу
     * @see com.rgmb.generator.entity.Author
     */
    private List<Author> authors;
    /**
     * Название книги
     */
    private String title;
    /**
     * Список жанров книги
     * @see com.rgmb.generator.entity.Genre
     */
    private List<Genre> genres;
    /**
     * Количество страниц в книге
     */
    private int size;
    /**
     * Описание книги
     */
    private String annotation;
    /**
     * Рейтинг книги
     */
    private double rating;
    /**
     * Год написания книги
     */
    private int year;
    /**
     * Url постера книги
     */
    private String imageName;

    /**
     * Пустой конструктор. Необходим для корректной работы Spring JDBC
     */
    public Book (){}

    /**
     * Конструктор со всеми полями
     * @param id id книги
     * @param authors список авторов {@link com.rgmb.generator.entity.Author}
     * @param title название
     * @param genres писок жанров {@link com.rgmb.generator.entity.Genre}
     * @param size количество страниц
     * @param annotation описание
     * @param rating рейтинг
     * @param year год написания
     * @param imageName url картинки
     */
    public Book(int id, List<Author> authors, String title, List<Genre> genres, int size, String annotation, double rating, int year, String imageName) {
        this.id = id;
        this.authors = authors;
        this.title = title;
        this.genres = genres;
        this.size = size;
        this.annotation = annotation;
        this.rating = rating;
        this.year = year;
        this.imageName = imageName;
    }

    /**
     * Конструктор без года написания, т.к. год написания можетт быть неопределён
     * @param id id книги
     * @param authors список авторов {@link com.rgmb.generator.entity.Author}
     * @param title название
     * @param genres список жанров {@link com.rgmb.generator.entity.Genre}
     * @param size количество страниц
     * @param annotation описание
     * @param rating рейтинг
     * @param imageName url картинки
     */
    public Book(int id, List<Author> authors, String title, List<Genre> genres, int size, String annotation, double rating, String imageName) {
        this.id = id;
        this.authors = authors;
        this.title = title;
        this.genres = genres;
        this.size = size;
        this.annotation = annotation;
        this.rating = rating;
        this.year = 0;
        this.imageName = imageName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

}
