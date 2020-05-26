package com.rgmb.generator.dao;

import java.util.List;

/**
 * Общий интерфейс для сущностей, связанных с играми
 */
public interface GeneralGameDAO<T> {
    T findById(int id);

    List<T> findAll();

    int add(T t);

    int deleteById(int id);
}
