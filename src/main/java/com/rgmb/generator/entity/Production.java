package com.rgmb.generator.entity;

/**
 * Класс сущности кинокомпания
 * Соответствующая таблица в базе данных - movie_genres
 */
public class Production  {
    /**
     * Название кинокомпании
     */
    private String name;
    /**
     * id кинокомпании
     */
    private int id;

    /**
     * Конструктор, принимающий все поля
     * @param id id кинокомпании
     * @param name название кинокомпании
     */
    public Production(int id,String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Коструктор, принимающий название кинокомпании
     * @param name название кинокомпании
     */
    public Production(String name) {
        id = 0;
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
