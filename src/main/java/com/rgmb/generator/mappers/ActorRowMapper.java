package com.rgmb.generator.mappers;

import com.rgmb.generator.entity.Actor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActorRowMapper implements RowMapper<Actor > {
    @Override
    public Actor mapRow(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("actor_id");
        String name = resultSet.getString("actor_name");
        return new Actor(id,name);
    }
}
