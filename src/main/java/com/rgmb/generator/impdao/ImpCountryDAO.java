package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.CountryDAO;
import com.rgmb.generator.entity.Country;
import com.rgmb.generator.mappers.CountryRowMapper;
import com.rgmb.generator.mappers.CountryRowMapperForFindId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("countryDAO")
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpCountryDAO implements CountryDAO {
    @Autowired
    JdbcTemplate template;

    @Override
    public int findIdByCountryTitle(String title) {
        String SQL = "SELECT id FROM countries WHERE LOWER(country_name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new CountryRowMapperForFindId(), title);
        }catch (EmptyResultDataAccessException ex){
            return 0;
        }
    }

    @Override
    public Country findById(int id) {
        String SQL = "SELECT * FROM countries WHERE id = ?";
        return template.queryForObject(SQL,new CountryRowMapper(),id);
    }

    @Override
    public int add(Country country) {
        String SQL = "INSERT INTO countries(country_name) VALUES (?)";
        return template.update(SQL, country.getName());
    }

    @Override
    public int addWithReturningId(Country country) {
        String SQL = "INSERT INTO countries(country_name) VALUES (?) RETURNING id";
        return template.queryForObject(SQL,new CountryRowMapperForFindId(), country.getName());
    }

    @Override
    public List<Country> findAll() {
        String SQL = "SELECT * FROM countries";
        return template.query(SQL,new CountryRowMapper());
    }

    @Override
    public int updateById(int id, Country country) {
        String SQL = "UPDATE countries SET country_name = ? WHERE id = ?";
        return template.update(SQL, country.getName(), id);
    }

    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM countries WHERE id = ?";
        return template.update(SQL,id);
    }
}
