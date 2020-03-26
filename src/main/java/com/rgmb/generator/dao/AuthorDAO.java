package com.rgmb.generator.dao;

import com.rgmb.generator.entity.Author;

import java.util.List;

public interface AuthorDAO extends GeneralBookDAO<Author>{
    int updateNameById(int id,String name);

    Author findByName(String name);


}
