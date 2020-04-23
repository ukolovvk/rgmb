package com.rgmb.generator.entity;

import java.util.List;

public class Game {
    private int id;
    private String title;
    private List<GameGenre> genres;
    private List<Country> countries;
    private GameCompany company;
    private int releaseYear;
    private String annotation;
    private String imageName;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }



    public Game(int id, String title, List<GameGenre> genres, List<Country> countries, GameCompany company, int releaseYear, String annotation, String imageName) {
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.countries = countries;
        this.company = company;
        this.releaseYear = releaseYear;
        this.annotation = annotation;
        this.imageName = imageName;
    }

    public Game(String title, List<GameGenre> genres, List<Country> countries, GameCompany company, int releaseYear, String annotation, String imageName) {
        this.id = 0;
        this.title = title;
        this.genres = genres;
        this.countries = countries;
        this.company = company;
        this.releaseYear = releaseYear;
        this.annotation = annotation;
        this.imageName = imageName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GameGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<GameGenre> genres) {
        this.genres = genres;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public GameCompany getCompany() {
        return company;
    }

    public void setCompany(GameCompany company) {
        this.company = company;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

}
