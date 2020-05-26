package com.rgmb.generator.dao;

/**
 * Интерфейс для класса, реализующщего логику работы с объединенной таблицей Фильмов и стран
 * См. реализацию в классе {@link com.rgmb.generator.impdao.ImpMovieUnionCountriesDAO}
 */
public interface MovieUnionCountriesDAO {
    int add(int movieID, int countryID);

    int delete(int movieID);
}
