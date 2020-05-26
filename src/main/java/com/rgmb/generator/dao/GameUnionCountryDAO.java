package com.rgmb.generator.dao;

/**
 * Интерфейс для класса, реализующщего логику работы с объединенной таблицей игр и стран
 * См. реализацию в классе {@link com.rgmb.generator.impdao.ImpGameUnionCountryDAO}
 */
public interface GameUnionCountryDAO {
    int add(int gameID, int countryID);

    int delete(int gameID);
}
