package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.GameGenreDAO;
import com.rgmb.generator.entity.GameGenre;
import com.rgmb.generator.mappers.GameGenreRowMapper;
import com.rgmb.generator.mappers.GameGenreRowMapperForFindById;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ImpGameGenreDAO implements GameGenreDAO {
    @Autowired
    JdbcTemplate template;

    @Override
    public int findIdByGameGenreName(String gameGenreName) {
        String SQL = "SELECT genre_id FROM game_genres WHERE LOWER(genre_name) = LOWER(?) RETURNING genre_id";
        try {
            return template.queryForObject(SQL, new GameGenreRowMapperForFindById(), gameGenreName);
        }catch (EmptyResultDataAccessException ex){
            return 0;
        }
    }

    @Override
    public int addWithReturningId(GameGenre genre) {
        String SQL = "INSERT INTO game_genres(genre_name) VALUES (?) RETURNING genre_id";
        return template.queryForObject(SQL,new GameGenreRowMapperForFindById(),genre.getName());
    }

    @Override
    public GameGenre findById(int id) {
        String SQL = "SELECT * FROM game_genres WHERE genre_id = ?";
        return template.queryForObject(SQL,new GameGenreRowMapper(),id);
    }

    @Override
    public List<GameGenre> findAll() {
        String SQL = "SELECT * FROM game_genres WHERE";
        return template.query(SQL,new GameGenreRowMapper());
    }

    @Override
    public int add(GameGenre genre) {
        String SQL = "INSERT INTO game_genres(genre_name) VALUES(?)";
        return template.update(SQL,genre.getName());
    }

    @Override
    public int updateById(int id, GameGenre gameGenre) {
        String SQL = "UPDATE game_genres SET genre_name = ? WHERE genre_id = ?";
        return template.update(SQL, gameGenre.getName(),id);
    }

    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM game_genres WHERE genre_id = ?";
        return template.update(SQL,id);
    }
}