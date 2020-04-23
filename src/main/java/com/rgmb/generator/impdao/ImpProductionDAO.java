package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.ProductionDAO;
import com.rgmb.generator.entity.Production;
import com.rgmb.generator.mappers.ProductionRowMapper;
import com.rgmb.generator.mappers.ProductionRowMapperForFindId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("ProductionDAO")
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpProductionDAO implements ProductionDAO {
    @Autowired
    JdbcTemplate template;

    @Override
    public int findIdByProductionName(String productionName) {
        String SQL = "SELECT id FROM productions WHERE LOWER(production_name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new ProductionRowMapperForFindId(), productionName);
        }catch (EmptyResultDataAccessException ex){
            return 0;
        }
    }

    @Override
    public int addWithReturningId(Production production) {
        String SQL = "INSERT INTO productions(production_name) VALUES(?) RETURNING id";
        return template.queryForObject(SQL,new ProductionRowMapperForFindId(), production.getName());
    }

    @Override
    public Production findById(int id) {
        String SQL = "SELECT * FROM productions WHERE id = ?";
        try {
            return template.queryForObject(SQL, new ProductionRowMapper(), id);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public int add(Production production) {
        String SQL = "INSERT INTO productions(production_name) VALUES(?)";
        return template.update(SQL,production.getName());
    }

    @Override
    public List<Production> findAll() {
        String SQL = "SELECT * FROM productions ";
        try {
            return template.query(SQL, new ProductionRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public int updateById(int id, Production production) {
        String SQL = "UPDATE productions SET production_name = ? WHERE id = ?";
        return template.update(SQL, production.getName(), id);
    }

    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM productions WHERE id = ?";
        return template.update(SQL,id);
    }
}
