package com.rgmb.generator.entity;
import java.util.List;

public class Book {
    private int id;
    private List<Author> authors;
    private String title;
    private List<Genre> genres;
    private int size;
    private String annotation;
    private double rating;
    private int year;
    private String imageName;

    public Book (){}

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

    public Book(List<Author> authors, String title, List<Genre> genres, int size, String annotation, double rating, int year, String imageName) {
        this.authors = authors;
        this.title = title;
        this.genres = genres;
        this.size = size;
        this.annotation = annotation;
        this.rating = rating;
        this.year = year;
        this.imageName = imageName;
    }

    public Book(List<Author> authors, String title, List<Genre> genres, int size, String annotation, double rating, String imageName) {
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
