package com.rgmb.generator.entity;

public class MovieGenre {
    private String name;
    private int id;

    public MovieGenre(int id,String name) {
        this.id = id;
        this.name = name;
    }

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
