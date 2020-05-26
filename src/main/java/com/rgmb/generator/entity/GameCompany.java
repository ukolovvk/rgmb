package com.rgmb.generator.entity;

/**
 * Класс сущности компания, создавшая игру
 * Соответствующая таблица в базе данных - company
 */
public class GameCompany {
    /**
     * id компании
     */
    private int id;
    /**
     * Название компании
     */
    private String name;

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
     * Конструктор,принимающий название компании
     * @param name название компании
     */
    public GameCompany(String name) {
        this.id = 0;
        this.name = name;
    }

    /**
     * Коструктор со всеми полями
     * @param id id компании
     * @param name название компании
     */
    public GameCompany(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
