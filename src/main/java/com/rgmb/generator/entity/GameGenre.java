package com.rgmb.generator.entity;

/**
 * Класс сущности жанр игры
 * Соответствующая таблица в базе данных - game_genres
 */
public class GameGenre {
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
    public GameGenre(int id, String name) {
        this.id = id;
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

    /**
     * Коструктор, принимающий название жанра
     * @param name название жанра
     */
    public GameGenre(String name) {
        this.id= 0;
        this.name = name;
    }
}
