package com.rgmb.generator.entity;

import java.util.Calendar;

public class Book {
    private int id;
    private Author author;
    private String title;
    private Genre genre;
    private int size;
    private String annotation;

    public Book(Author author, String title, Genre genre, int size, String annotation) {
        this.author = author;
        this.title = title;
        this.genre = genre;
        this.size = size;
        this.annotation = annotation;
    }

    public Book(Author author, String title, Genre genre){
        this.author = author;
        this.title = title;
        this.genre = genre;
        size = 0;
        annotation = "";
    }

    public Book(Author author, String title, Genre genre,int size){
        this.author = author;
        this.title = title;
        this.genre = genre;
        this.size = size;
        annotation = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
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


}
