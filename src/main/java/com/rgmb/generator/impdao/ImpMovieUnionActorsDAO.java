package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.MovieUnionActorsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("MovieUnionActorsDAO")
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpMovieUnionActorsDAO implements MovieUnionActorsDAO {
    @Autowired
    JdbcTemplate template;

    @Override
    public int add(int movieID, int actorID) {
        String SQL = "INSERT INTO movie_union_actors(movie_id, actor_id) VALUES(?,?)";
        return template.update(SQL,movieID,actorID);
    }

    @Override
    public int delete(int movieID) {
        String SQL = "DELETE FROM movie_union_actors WHERE movie_id = ? ";
        return template.update(SQL,movieID);
    }
}
