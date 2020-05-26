package com.rgmb.generator.entity;

/**
 * Класс сущности жанр фильма
 * Соответствующая таблица в базе данных - movie_genres
 */
public class MovieGenre {
    /**
     * Название жанра
     */
    private String name;
    /**
     * id жанра
     */
    private int id;

    /**
     * Конструктор со всеми полями
     * @param id id жанра
     * @param name название жанра
     */
    public MovieGenre(int id,String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Коструктор, принимающий название жанра
     * @param name название жанра
     */
    public MovieGenre(String name) {
        this.id = 0;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
