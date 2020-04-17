package com.rgmb.generator.entity;

public class Production  {
    private String name;
    private int id;

    public Production(int id,String name) {
        this.id = id;
        this.name = name;
    }

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
