package com.rgmb.generator.dao;

import com.rgmb.generator.entity.MovieGenre;

public interface MovieGenreDAO extends GeneralMovieDAO<MovieGenre> {
    int findIdByMovieGenreName(String movieGenreName);

    int addWithReturningId(MovieGenre movieGenre);
}
