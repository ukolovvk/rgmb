package com.rgmb.generator.mappers;

import com.rgmb.generator.entity.Country;
import com.rgmb.generator.entity.Game;
import com.rgmb.generator.entity.GameCompany;
import com.rgmb.generator.entity.GameGenre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GameRowMapper implements RowMapper<Game> {
    @Override
    public Game mapRow(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        String[] genres = resultSet.getString("genres").split(",");
        ArrayList<GameGenre> resultGenres = new ArrayList<>();
        for(String genre : genres){
            resultGenres.add(new GameGenre(genre));
        }
        String[] countries = resultSet.getString("countries").split(",");
        ArrayList<Country> resultCountries = new ArrayList<>();
        for(String country : countries){
            resultCountries.add(new Country(country));
        }
        String companyStr = resultSet.getString("company");
        if(companyStr == null)
            companyStr = "";
        GameCompany company = new GameCompany(companyStr);
        Integer releaseYear = resultSet.getInt("release_year");
        if(releaseYear == null)
            releaseYear = 0;
        String annotation = resultSet.getString("annotation");
        if(annotation == null)
            annotation = "";
        return new Game(id,title, resultGenres,resultCountries,company,releaseYear,annotation);
    }
}
