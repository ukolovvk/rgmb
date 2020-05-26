package com.rgmb.generator.entity;

/**
 * Класс сущности жанр книги
 * Соответствующая таблица в базе данных - genre
 */
public class Genre {
    /**
     * id жанра
     */
    private int id;
    /**
     * Название жанра
     */
    private String name;

    /**
     * Конструктор со всеми полями
     * @param id id жанра
     * @param name название жанра
     */
    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Коструктор, принимающий название жанра
     * @param name название жанра
     */
    public Genre(String name) {
        this.id = 0;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
