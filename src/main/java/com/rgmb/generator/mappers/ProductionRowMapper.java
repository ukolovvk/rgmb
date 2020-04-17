package com.rgmb.generator.mappers;

import com.rgmb.generator.entity.Production;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductionRowMapper implements RowMapper<Production> {
    @Override
    public Production mapRow(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("id");
        String title = resultSet.getString("production_name");
        return new Production(id,title);
    }
}
