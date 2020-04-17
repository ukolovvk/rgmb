package com.rgmb.generator.dao;

public interface BookUnionGenresDAO {

    int add(int bookID, int genreID);

    int delete(int bookID);

}
