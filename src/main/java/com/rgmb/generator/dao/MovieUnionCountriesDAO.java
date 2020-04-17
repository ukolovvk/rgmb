package com.rgmb.generator.dao;

public interface MovieUnionCountriesDAO {
    int add(int movieID, int countryID);

    int delete(int movieID);
}
