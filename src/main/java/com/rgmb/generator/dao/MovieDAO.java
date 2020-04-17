package com.rgmb.generator.dao;

import com.rgmb.generator.entity.*;
import com.rgmb.generator.exceptions.DaoException;
import com.rgmb.generator.exceptions.IncorrectParametersDaoException;

import java.util.List;

public interface MovieDAO extends GeneralMovieDAO<Movie> {
    Movie getRandomMovie();

    List<Movie> getRandomMovies(int numberOfMovies) ;

    Movie getRandomMovie(MovieGenre genre) throws DaoException;

    Movie getRandomMovie(int firstYear, int secondYear) throws DaoException;

    Movie getRandomMovie(MovieGenre genre, int firstYear, int secondYear) throws DaoException;

    Movie getRandomMovie(Production production) throws DaoException;

    Movie getRandomMovie(MovieGenre genre, Production production) throws DaoException;

    Movie getRandomMovie(int firstYear, int secondYear, Production production) throws DaoException;

    Movie getRandomMovie(MovieGenre genre, int firstYear, int secondYear, Production production) throws DaoException;

    int updateTitleById(int id, String title);

    int updateRatingById(int id, double rating) throws IncorrectParametersDaoException;

    int updateRuntimeById(int id, int runtime) throws IncorrectParametersDaoException;

    int updateDateReleaseById(int id, int year) throws IncorrectParametersDaoException;

    int updateProductionById(int id, Production production);

    int updateAnnotationById(int id, String annotation);

    int updateImageURLById(int id, String url);

    int updateGenreById(int id, List<MovieGenre> genres);

    int updateActorsById(int id, List<Actor> actors);

    int updateCountriesById(int id, List<Country> countries);

}
