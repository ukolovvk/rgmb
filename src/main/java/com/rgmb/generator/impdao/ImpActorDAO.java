package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.ActorDAO;
import com.rgmb.generator.entity.Actor;
import com.rgmb.generator.mappers.ActorRowMapper;
import com.rgmb.generator.mappers.ActorRowMapperForFindId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("actorDAO")
public class ImpActorDAO implements ActorDAO {
    @Autowired
    JdbcTemplate template;

    @Override
    public int findIdByActorName(String actorName) {
        String SQL = "SELECT actor_id FROM actors WHERE LOWER(actor_name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new ActorRowMapperForFindId(), actorName);
        }catch (EmptyResultDataAccessException ex){
            return 0;
        }
    }

    @Override
    public int addWithReturningId(Actor actor) {
        String SQL = "INSERT INTO actors(actor_name) VALUES (?) RETURNING actor_id";
        return template.queryForObject(SQL, new ActorRowMapperForFindId(), actor.getName());
    }

    @Override
    public Actor findById(int id) {
        String SQL = "SELECT * FROM actors WHERE id = ?";
        return template.queryForObject(SQL,new ActorRowMapper(),id);
    }

    @Override
    public int add(Actor actor) {
        String SQL = "INSERT INTO actors(actor_name) VALUES (?)";
        return template.update(SQL, actor.getName());
    }

    @Override
    public List<Actor> findAll() {
        String SQL = "SELECT * FROM actors";
        return template.query(SQL,new ActorRowMapper());
    }

    @Override
    public int updateById(int id, Actor actor) {
        String SQL = "UPDATE actors SET actor_name = ? WHERE id = ?";
        return template.update(SQL,actor.getName(),id);
    }

    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM actors WHERE id = ?";
        return template.update(SQL, id);
    }


}
