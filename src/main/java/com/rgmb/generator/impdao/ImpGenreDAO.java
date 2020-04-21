package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.GenreDAO;
import com.rgmb.generator.entity.Genre;
import com.rgmb.generator.mappers.GenreRowMapper;
import com.rgmb.generator.mappers.GenreRowMapperForFindByGenreName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("genreDAO")
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpGenreDAO implements GenreDAO {
    @Autowired
    private JdbcTemplate template;

    @Override
    public Genre findById(int id) {
        String SQL = "SELECT * FROM genre WHERE id = ?";
        return template.queryForObject(SQL,new GenreRowMapper(),id);
    }

    @Override
    public List<Genre> findAll() {
        String SQL = "SELECT * FROM genre ";
        return template.query(SQL,new GenreRowMapper());
    }

    @Override
    public int add(Genre genre) {
        String SQL = "INSERT INTO genre (genre_name) VALUES (?) ";
        return template.update(SQL,genre.getName());
    }

    @Override
    public int deleteById(int id) {
        String SQL =  "DELETE FROM genre WHERE id = ?";
        return template.update(SQL,id);
    }

    @Override
    public int updateNameGenre(int id,String genreName) {
        String SQL = "UPDATE genre SET genre_name = ? WHERE id = ?";
        return template.update(SQL,genreName,id);
    }

    @Override
    public Genre findByNameGenre(String nameGenre) {
        String SQL = "SELECT * FROM genre WHERE LOWER(genre_name) = LOWER(?)";
        return template.queryForObject(SQL,new GenreRowMapper(),nameGenre);
    }

    @Override
    public int addWithReturningId(Genre genre) {
        String SQL = "INSERT INTO genre(genre_name) VALUES(?) RETURNING id";
        return  template.queryForObject(SQL,new GenreRowMapperForFindByGenreName(), genre.getName());
    }

    @Override
    public int findIdByGenreName(String genreName) {
        String SQL = "SELECT * FROM genre WHERE LOWER(genre_name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new GenreRowMapperForFindByGenreName(), genreName);
        }catch(EmptyResultDataAccessException ex){
            return 0;
        }
    }
}
