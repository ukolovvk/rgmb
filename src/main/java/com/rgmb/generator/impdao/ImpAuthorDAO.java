package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.AuthorDAO;
import com.rgmb.generator.entity.Author;
import com.rgmb.generator.mappers.AuthorRowMapper;
import com.rgmb.generator.mappers.AuthorRowMapperForFindByAuthorName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("authorDAO")
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpAuthorDAO implements AuthorDAO {
    @Autowired
    private JdbcTemplate template;

    @Override
    public Author findById(int id) {
        String SQL = "SELECT * FROM authors WHERE id = ?";
        try {
            return template.queryForObject(SQL, new AuthorRowMapper(), id);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Author findByName(String name) {
        String SQL = "SELECT * FROM authors WHERE LOWER(name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new AuthorRowMapper(), name);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public int addWithReturningId(Author author) {
        String SQL = "INSERT INTO authors(name) VALUES(?) RETURNING id";
        return template.queryForObject(SQL,new AuthorRowMapperForFindByAuthorName(),author.getName());
    }

    @Override
    public int findIdByAuthorName(String authorName) {
        String SQL = "SELECT * FROM authors WHERE LOWER(name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new AuthorRowMapperForFindByAuthorName(), authorName);
        }catch (EmptyResultDataAccessException ex){
            return 0;
        }
    }

    @Override
    public List<Author> findAll() {
        String SQL = "SELECT * FROM authors";
        try {
            return template.query(SQL, new AuthorRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public int add(Author author) {
        String SQL = "INSERT INTO authors (name) VALUES(?)";
        return template.update(SQL,author.getName());
    }

    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM authors WHERE id = ?";
        return template.update(SQL,id);
    }

    @Override
    public int updateNameById(int id, String name) {
        String SQL = "UPDATE authors SET name = ? WHERE id = ?";
        return template.update(SQL,name,id);
    }

}
