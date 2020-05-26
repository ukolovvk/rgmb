package com.rgmb.generator.dao;

import com.rgmb.generator.entity.MovieGenre;

/**
 * Интерфейс для сущности - жанр фильма
 * См реализацию в классе {@link com.rgmb.generator.impdao.ImpMovieGenreDAO}
 */
public interface MovieGenreDAO extends GeneralMovieDAO<MovieGenre> {
    int findIdByMovieGenreName(String movieGenreName);

    int addWithReturningId(MovieGenre movieGenre);
}
