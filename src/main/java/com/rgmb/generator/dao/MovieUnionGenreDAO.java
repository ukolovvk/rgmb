package com.rgmb.generator.dao;

/**
 * Интерфейс для класса, реализующщего логику работы с объединенной таблицей Фильмов и жанров
 * См. реализацию в классе {@link com.rgmb.generator.impdao.ImpMovieUnionGenreDAO}
 */
public interface MovieUnionGenreDAO {
    int add(int movieID, int movieGenreID);

    int delete(int movieID);
}
