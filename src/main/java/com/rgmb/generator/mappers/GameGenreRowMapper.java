package com.rgmb.generator.mappers;

import com.rgmb.generator.entity.GameGenre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameGenreRowMapper implements RowMapper<GameGenre> {
    @Override
    public GameGenre mapRow(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("genre_id");
        String name = resultSet.getString("genre_name");
        return new GameGenre(id,name);
    }
}
