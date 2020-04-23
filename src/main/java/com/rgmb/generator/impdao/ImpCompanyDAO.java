package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.CompanyDAO;
import com.rgmb.generator.entity.GameCompany;
import com.rgmb.generator.mappers.CompanyRowMapperForFindById;
import com.rgmb.generator.mappers.GameCompanyRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("CompanyDAO")
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpCompanyDAO implements CompanyDAO {
    @Autowired
    JdbcTemplate template;

    @Override
    public int findIdByGameCompanyName(String gameCompanyName) {
        String SQL = "SELECT company_id FROM company WHERE LOWER(company_name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new CompanyRowMapperForFindById(), gameCompanyName);
        }catch (EmptyResultDataAccessException ex){
            return 0;
        }
    }

    @Override
    public int addWithReturningId(GameCompany company) {
        String SQL = "INSERT INTO company(company_name) VALUES (?) RETURNING company_id";
        return template.queryForObject(SQL,new CompanyRowMapperForFindById(),company.getName());
    }

    @Override
    public GameCompany findById(int id) {
        String SQL = "SELECT * FROM company WHERE company_id = ?";
        try {
            return template.queryForObject(SQL, new GameCompanyRowMapper(), id);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public List<GameCompany> findAll() {
        String SQL = "SELECT * FROM company";
        try {
            return template.query(SQL, new GameCompanyRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public int add(GameCompany company) {
        String SQL = "INSERT INTO company(company_name) VALUES(?)";
        return template.update(SQL,company.getName());
    }

    @Override
    public int updateById(int id, GameCompany gameCompany) {
        String SQL = "UPDATE company SET company_name = ? WHERE company_id = ?";
        return template.update(SQL, gameCompany.getName(),id);
    }

    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM company WHERE company_id = ?";
        return template.update(SQL,id);
    }
}
