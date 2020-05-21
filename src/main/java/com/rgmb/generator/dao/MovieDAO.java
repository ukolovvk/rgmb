package com.rgmb.generator.dao;

import com.rgmb.generator.entity.*;

import java.util.List;

public interface MovieDAO extends GeneralMovieDAO<Movie> {
    Movie getRandomMovie();

    Movie getRandomMovie(MovieGenre genre);

    Movie getRandomMovie(int firstYear, int secondYear) ;

    Movie getRandomMovie(MovieGenre genre, int firstYear, int secondYear) ;

    Movie getRandomMovie(Production production) ;

    Movie getRandomMovie(MovieGenre genre, Production production) ;

    Movie getRandomMovie(int firstYear, int secondYear, Production production) ;

    Movie getRandomMovie(MovieGenre genre, int firstYear, int secondYear, Production production) ;

    int updateTitleById(int id, String title);

    int updateRatingById(int id, double rating) ;

    int updateRuntimeById(int id, int runtime);

    int updateDateReleaseById(int id, int year);

    int updateProductionById(int id, Production production);

    int updateAnnotationById(int id, String annotation);

    int updateImageURLById(int id, String url);

    int updateGenreById(int id, List<MovieGenre> genres);

    int updateActorsById(int id, List<Actor> actors);

    int updateCountriesById(int id, List<Country> countries);

}
