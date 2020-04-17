package com.rgmb.generator.dao;

import java.util.List;

public interface GeneralMovieDAO<T>{

    T findById(int id);

    int add(T t);

    List<T> findAll();

    int updateById(int id,T t);

    int deleteById(int id);
}
