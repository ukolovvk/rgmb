package com.rgmb.generator.dao;

public interface MovieUnionGenreDAO {
    int add(int movieID, int movieGenreID);

    int delete(int movieID);
}
