package com.rgmb.generator.dao;

import java.util.List;

public interface GeneralGameDAO<T> {
    T findById(int id);

    List<T> findAll();

    int add(T t);

    int updateById(int id,T t);

    int deleteById(int id);
}
