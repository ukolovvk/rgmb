package com.rgmb.generator.entity;

import java.util.List;

/**
 * Класс сущности страна
 * Соответствующая таблица в базе данных - games
 */
public class Game {
    /**
     * id игры в базе данных
     */
    private int id;
    /**
     * Название игры
     */
    private String title;
    /**
     * Список жанров игры
     * @see com.rgmb.generator.entity.GameGenre
     */
    private List<GameGenre> genres;
    /**
     * Список стран, в которых была создана игра
     * @see com.rgmb.generator.entity.Country
     */
    private List<Country> countries;
    /**
     * Компания, которая создала игру
     * @see com.rgmb.generator.entity.GameCompany
     */
    private GameCompany company;
    /**
     * Год выпуска игры
     */
    private int releaseYear;
    /**
     * Описание игры
     */
    private String annotation;
    /**
     * Url постера игры
     */
    private String imageName;

    /**
     * Пустой конструктор, необходимый для корректной работы Spring JDBC
     */
    public Game() {}

    /**
     * Конструктор со всеми полями
     * @param id id игры
     * @param title название игры
     * @param genres список жанров {@link com.rgmb.generator.entity.GameGenre}
     * @param countries список стран {@link com.rgmb.generator.entity.Country}
     * @param company компания {@link com.rgmb.generator.entity.GameCompany}
     * @param releaseYear год выпуска
     * @param annotation описание
     * @param imageName url постера
     */
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

    /**
     * Конструктор без id
     * @param title название игры
     * @param genres список жанров {@link com.rgmb.generator.entity.GameGenre}
     * @param countries список стран {@link com.rgmb.generator.entity.Country}
     * @param company компания {@link com.rgmb.generator.entity.GameCompany}
     * @param releaseYear год выпуска
     * @param annotation описание
     * @param imageName url постера
     */
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

}
