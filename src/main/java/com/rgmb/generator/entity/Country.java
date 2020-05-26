package com.rgmb.generator.entity;

/**
 * Класс сущности страна
 * Соответствующая таблица в базе данных - countries
 */
public class Country {
    /**
     * Название страна
     */
    private String name;
    /**
     * id страны в базе данных
     */
    private int id;

    /**
     * Конструктор со всеми полями
     * @param id Id страны
     * @param name название страны
     */
    public Country(int id,String name) {
        this.name = name;
        this.id = id;
    }

    /**
     * Конструктор,принимающий название страны
     * @param name название страны
     */
    public Country(String name) {
        this.name = name;
        this.id = 0;
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
