package com.rgmb.generator.dao;

import java.util.List;

/**
 * Общий интерфейс для сущностей, связанных с фильмами
 */
public interface GeneralMovieDAO<T>{

    T findById(int id);

    int add(T t);

    List<T> findAll();

    int updateById(int id,T t);

    int deleteById(int id);
}
