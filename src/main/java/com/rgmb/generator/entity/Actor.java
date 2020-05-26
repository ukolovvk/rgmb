package com.rgmb.generator.entity;

/**
 * Класс сущности актер
 * Соответствующая таблица в базе данных - actors
 */
public class Actor {
    /**
     * Фио актера
     */
    private String name;
    /**
     * id актера в базе данных
     */
    private int id;

    /**
     * Конструктор со всеми полями
     * @param id id актера
     * @param name фио актера
     */
    public Actor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Конструктор, принимающий только фио актера
     * @param name фио актера
     */
    public Actor(String name) {
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
