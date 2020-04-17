package com.rgmb.generator.mappers;

import com.rgmb.generator.entity.GameCompany;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameCompanyRowMapper implements RowMapper<GameCompany> {
    @Override
    public GameCompany mapRow(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("company_id");
        String name = resultSet.getString("company_name");
        return new GameCompany(id,name);
    }
}
