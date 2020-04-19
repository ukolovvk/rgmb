package com.rgmb.generator.dao;

public interface GameUnionGenreDAO {
    int add(int gameID, int gameGenreID);

    int delete(int gameID);
}
