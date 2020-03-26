package com.rgmb.generator.dao;
import java.util.List;

public interface GeneralBookDAO <T> {

    T findById(int id);

    List<T> findAll();

    int add(T book);

    int deleteById(int id);

}
