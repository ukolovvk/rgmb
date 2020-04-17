package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.MovieUnionGenreDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("MovieUnionGenreDAO")
public class ImpMovieUnionGenreDAO implements MovieUnionGenreDAO {
    @Autowired
    JdbcTemplate template;

    @Override
    public int add(int movieID, int movieGenreID) {
        String SQL = "INSERT INTO movie_union_genres(movie_id,genre_id) VALUES(?,?)";
        return template.update(SQL,movieID,movieGenreID);
    }

    @Override
    public int delete(int movieID) {
        String SQL = "DELETE  FROM movie_union_genres WHERE movie_id = ? ";
        return template.update(SQL,movieID);
    }
}
