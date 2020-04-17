package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.MovieUnionCountriesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("MovieUnionCountriesDAO")
public class ImpMovieUnionCountriesDAO implements MovieUnionCountriesDAO {
    @Autowired
    JdbcTemplate template;

    @Override
    public int delete(int movieID) {
        String SQL = "DELETE FROM movie_union_countries WHERE movie_id = ?";
        return template.update(SQL, movieID);
    }

    @Override
    public int add(int movieID, int countryID) {
        String SQL = "INSERT INTO movie_union_countries(movie_id, country_id) VALUES (?,?)";
        return template.update(SQL,movieID,countryID);
    }
}
