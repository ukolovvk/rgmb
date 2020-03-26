package com.rgmb.generator.mappers;

import com.rgmb.generator.entity.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreRowMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("id");
        String genreName = resultSet.getString("genre_name");
        return new Genre(id,genreName);
    }
}
