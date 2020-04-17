package com.rgmb.generator.entity;

public class Country {
    private String name;
    private int id;

    public Country(int id,String name) {
        this.name = name;
        this.id = id;
    }

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
