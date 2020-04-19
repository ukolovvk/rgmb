package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.GameUnionCountryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("ImpGameUnionCountryDAO")
public class ImpGameUnionCountryDAO implements GameUnionCountryDAO {
    @Autowired
    private JdbcTemplate template;

    @Override
    public int add(int gameID, int countryID) {
        String SQL = "INSERT INTO games_union_countries(game_id,country_id) VALUES(?,?)";
        return template.update(SQL,gameID,countryID);
    }

    @Override
    public int delete(int gameID) {
        String SQL = "DELETE FROM games_union_countries WHERE game_id = ?";
        return template.update(SQL,gameID);
    }
}
