package com.rgmb.generator.dao;

import com.rgmb.generator.entity.Country;

import java.util.List;

/**
 * Интерфейс для сущности - страна
 * См реализацию в классе {@link com.rgmb.generator.impdao.ImpCountryDAO}
 */
public interface CountryDAO {
    int findIdByCountryTitle(String title);

    Country findById(int id);

    int add(Country country);

    int addWithReturningId(Country country);

    List<Country> findAll();

    int updateById(int id,Country country);

    int deleteById(int id);
}
