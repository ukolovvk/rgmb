package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.GameUnionGenreDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("ImpGameUnionGenreDAO")
public class ImpGameUnionGenreDAO implements GameUnionGenreDAO {
    @Autowired
    private JdbcTemplate template;

    @Override
    public int add(int gameID, int gameGenreID) {
        String SQL = "INSERT INTO games_union_genres(game_id,genre_id) VALUES(?,?)";
        return template.update(SQL,gameID,gameGenreID);
    }

    @Override
    public int delete(int gameID) {
        String SQL = "DELETE FROM games_union_genres WHERE game_id = ?";
        return template.update(SQL,gameID);
    }
}
