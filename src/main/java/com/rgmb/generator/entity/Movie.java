package com.rgmb.generator.entity;

import java.util.Calendar;
import java.util.List;

/**
 * Класс сущности фильм
 * Соответствующая таблица в базе данных - movies
 */
public class Movie {
    /**
     * id фильма
     */
    private long id;
    /**
     * название фильма
     */
    private String title;
    /**
     * Год выпуска
     */
    private int releaseDate;
    /**
     * Список стран
     * @see com.rgmb.generator.entity.Country
     */
    private List<Country> countryList;
    /**
     * Кинокомпания
     * @see com.rgmb.generator.entity.Production
     */
    private Production production;
    /**
     * Список жанров фильма
     * @see com.rgmb.generator.entity.MovieGenre
     */
    private List<MovieGenre> genreList;
    /**
     * Рейтинг фильма
     */
    private double rating;
    /**
     * Продолжительность фильма
     */
    private int runtime;
    /**
     * Список актеров
     * @see com.rgmb.generator.entity.Actor
     */
    private List<Actor> listActors;
    /**
     * Описание
     */
    private String annotation;
    /**
     * url постера
     */
    private String urlImage;

    /**
     * Пустой конструктор, необходим для корректной работы Spring JDBC
     */
    public Movie(){}

    /**
     * Конструктор, принимающий все поля
     * @param id id фильма
     * @param title название фильма
     * @param releaseDate год выпуска
     * @param countryList список стран {@link com.rgmb.generator.entity.Country}
     * @param production кинокомпания {@link com.rgmb.generator.entity.Production}
     * @param genreList список жанров {@link com.rgmb.generator.entity.MovieGenre}
     * @param rating рейтинг
     * @param runtime продолжительность
     * @param listActors список актеров {@link com.rgmb.generator.entity.Actor}
     * @param annotation описание
     * @param urlImage url постера
     */
    public Movie(long id, String title, int releaseDate, List<Country> countryList, Production production, List<MovieGenre> genreList, double rating, int runtime, List<Actor> listActors, String annotation, String urlImage) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.countryList = countryList;
        this.production = production;
        this.genreList = genreList;
        this.rating = rating;
        this.runtime = runtime;
        this.listActors = listActors;
        this.annotation = annotation;
        this.urlImage = urlImage;
    }

    /**
     * Конструктор, принимающий все поля, кроме id
     * @param title название фильма
     * @param releaseDate год выпуска
     * @param countryList список стран {@link com.rgmb.generator.entity.Country}
     * @param production кинокомпания {@link com.rgmb.generator.entity.Production}
     * @param genreList список жанров {@link com.rgmb.generator.entity.MovieGenre}
     * @param rating рейтинг
     * @param runtime продолжительность
     * @param listActors список актеров {@link com.rgmb.generator.entity.Actor}
     * @param annotation описание
     * @param urlImage url постера
     */
    public Movie(String title, int releaseDate, List<Country> countryList, Production production, List<MovieGenre> genreList, double rating, int runtime, List<Actor> listActors, String annotation, String urlImage) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.countryList = countryList;
        this.production = production;
        this.genreList = genreList;
        this.rating = rating;
        this.runtime = runtime;
        this.listActors = listActors;
        this.annotation = annotation;
        this.urlImage = urlImage;
        this.id = 0;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public int getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(int releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Production getProduction() {
        return production;
    }

    public void setProduction(Production production) {
        this.production = production;
    }

    public List<MovieGenre> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<MovieGenre> genreList) {
        this.genreList = genreList;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public List<Actor> getListActors() {
        return listActors;
    }

    public void setListActors(List<Actor> listActors) {
        this.listActors = listActors;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
}
