package com.rgmb.generator.dao;

public interface GameUnionCountryDAO {
    int add(int gameID, int countryID);

    int delete(int gameID);
}
