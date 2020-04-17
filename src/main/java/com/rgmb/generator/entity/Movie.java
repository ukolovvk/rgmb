package com.rgmb.generator.entity;

import java.util.Calendar;
import java.util.List;

public class Movie {
    private long id;
    private String title;
    private int releaseDate;
    private List<Country> countryList;
    private Production production;
    private List<MovieGenre> genreList;
    private double rating;
    private int runtime;
    private List<Actor> listActors;
    private String annotation;
    private String urlImage;

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
