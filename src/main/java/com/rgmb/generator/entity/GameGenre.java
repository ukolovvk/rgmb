package com.rgmb.generator.entity;

public class GameGenre {
    private int id;
    private String name;

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

    public GameGenre(String name) {
        this.id= 0;
        this.name = name;
    }
}
