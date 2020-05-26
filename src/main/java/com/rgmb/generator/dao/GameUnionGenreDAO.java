package com.rgmb.generator.dao;

/**
 * Интерфейс для класса, реализующщего логику работы с объединенной таблицей игр и жанров
 * См. реализацию в классе {@link com.rgmb.generator.impdao.ImpGameUnionGenreDAO}
 */
public interface GameUnionGenreDAO {
    int add(int gameID, int gameGenreID);

    int delete(int gameID);
}
