package com.rgmb.generator.dao;
import java.util.List;

/**
 * Общий интерфейс для сущностей, связанных с книгами
 */
public interface GeneralBookDAO <T> {

    T findById(int id);

    List<T> findAll();

    int add(T book);

    int deleteById(int id);

}
