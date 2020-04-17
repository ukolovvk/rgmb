package com.rgmb.generator.dao;

import com.rgmb.generator.entity.Country;

import java.util.List;

public interface CountryDAO {
    int findIdByCountryTitle(String title);

    Country findById(int id);

    int add(Country country);

    int addWithReturningId(Country country);

    List<Country> findAll();

    int updateById(int id,Country country);

    int deleteById(int id);
}
