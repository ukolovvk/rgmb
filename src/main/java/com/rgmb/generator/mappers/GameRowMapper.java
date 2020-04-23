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
        int id = resultSet.getInt("game_id");
        String title = resultSet.getString("title");
        String genres = resultSet.getString("genres");
        ArrayList<GameGenre> resultGenres = new ArrayList<>();
        if(genres != null) {
            for (String genre : genres.split(",")) {
                resultGenres.add(new GameGenre(genre));
            }
        }
        String countries = resultSet.getString("countries");
        ArrayList<Country> resultCountries = new ArrayList<>();
        if(countries != null) {
            for (String country : countries.split(",")) {
                resultCountries.add(new Country(country));
            }
        }
        String companyStr = resultSet.getString("company_name");
        if(companyStr == null)
            companyStr = "";
        GameCompany company = new GameCompany(companyStr);
        Integer releaseYear = resultSet.getInt("release_year");
        if(releaseYear == null)
            releaseYear = 0;
        String annotation = resultSet.getString("annotation");
        if(annotation == null)
            annotation = "";
        String imageName = resultSet.getString("image");
        if(imageName == null)
            imageName = "";
        return new Game(id,title, resultGenres,resultCountries,company,releaseYear,annotation,imageName);
    }
}
