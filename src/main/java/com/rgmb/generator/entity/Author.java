package com.rgmb.generator.entity;

/**
 * Класс сущности актер
 * Соответствующая таблица в базе данных - authors
 */
public class Author {
    /**
     * id автора в базе данных
     */
    private int id;
    /**
     * Фио автора
     */
    private String name;

    /**
     * Конструктор со всеми полями
     * @param id id автора
     * @param name фио автора
     */
    public Author(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Конструктор, принимающий только фио автора
     * @param name фио автора
     */
    public Author(String name) {
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
