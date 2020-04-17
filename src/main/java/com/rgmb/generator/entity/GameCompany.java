package com.rgmb.generator.entity;

public class GameCompany {
    private int id;
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

    public GameCompany(String name) {
        this.id = 0;
        this.name = name;
    }

    public GameCompany(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
