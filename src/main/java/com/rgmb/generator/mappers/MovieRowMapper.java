package com.rgmb.generator.mappers;

import com.rgmb.generator.entity.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieRowMapper implements RowMapper<Movie> {
    @Override
    public Movie mapRow(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("movie_id");
        String title = resultSet.getString("movie_title");
        String productionName = resultSet.getString("production_name");
        Production localProduction = null;
        if(productionName == null || productionName.isEmpty()){
            localProduction = new Production("noname");
        }else{
            localProduction = new Production(productionName);
        }

        List<MovieGenre> ListGenres =  new ArrayList<>();
        String resultGenres = resultSet.getString("genres");
        List<String> localListGenres = new ArrayList<>();
        if(resultGenres != null)
            localListGenres = Arrays.asList(resultGenres.split(","));
        for(String localStr : localListGenres)
            ListGenres.add(new MovieGenre(localStr));

        List<Actor> ListActors = new ArrayList<>();
        String resultActors = resultSet.getString("actors");
        List<String> localListActors = new ArrayList<>();
        if(resultActors != null)
            localListActors = Arrays.asList(resultActors.split(","));
        for(String localStr : localListActors)
            ListActors.add(new Actor(localStr));

        List<Country> countriesList = new ArrayList<>();
        String resultCountries = resultSet.getString("countries");
        List<String> localListCountries = new ArrayList<>();
        if(resultCountries != null)
            localListCountries = Arrays.asList(resultCountries.split(","));
        for(String localStr : localListCountries)
            countriesList.add(new Country(localStr));

        double rating = resultSet.getDouble("rating");
        String annotation = resultSet.getString("annotation");
        if(annotation == null)
            annotation = "";
        int runtime = resultSet.getInt("runtime");
        String urlImage = resultSet.getString("image");
        if(urlImage == null)
            urlImage = "";
        int releaseYear = resultSet.getInt("release_date");

        return new Movie(id,title,releaseYear,countriesList,localProduction,ListGenres,rating,runtime,ListActors,annotation,urlImage);
    }
}
