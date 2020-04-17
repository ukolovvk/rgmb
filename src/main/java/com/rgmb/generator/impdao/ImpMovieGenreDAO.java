package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.MovieGenreDAO;
import com.rgmb.generator.entity.MovieGenre;
import com.rgmb.generator.mappers.MovieGenreRowMapper;
import com.rgmb.generator.mappers.MovieGenreRowMapperForFindId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("movieGenreDAO")
public class ImpMovieGenreDAO implements MovieGenreDAO {
    @Autowired
    JdbcTemplate template;

    @Override
    public int findIdByMovieGenreName(String movieGenreName) {
        String SQL = "SELECT genre_id FROM movie_genres WHERE LOWER(genre_name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new MovieGenreRowMapperForFindId(), movieGenreName);
        }catch (EmptyResultDataAccessException ex){
            return 0;
        }
    }

    @Override
    public int addWithReturningId(MovieGenre movieGenre) {
        String SQL = "INSERT INTO movie_genres(genre_name) VALUES (?) RETURNING genre_id";
        return template.queryForObject(SQL,new MovieGenreRowMapperForFindId(), movieGenre.getName());
    }

    @Override
    public MovieGenre findById(int id) {
        String SQL = "SELECT * FROM movie_genres WHERE genre_id = ?";
        return template.queryForObject(SQL, new MovieGenreRowMapper(),id);
    }

    @Override
    public int add(MovieGenre movieGenre) {
        String SQL = "INSERT INTO movie_genres(genre_name) VALUES (?)";
        return template.update(SQL,movieGenre.getName());
    }

    @Override
    public List<MovieGenre> findAll() {
        String SQL = "SELECT * FROM movie_genres";
        return template.query(SQL, new MovieGenreRowMapper());
    }

    @Override
    public int updateById(int id, MovieGenre movieGenre) {
        String SQL = "UPDATE movie_genres SET genre_name = ? WHERE genre_id = ?";
        return template.update(SQL, movieGenre.getName(),id);
    }

    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM movie_genre WHERE genre_id = ?";
        return template.update(SQL,id);
    }


}
