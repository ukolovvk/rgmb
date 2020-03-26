package com.rgmb.generator.dao;

import com.rgmb.generator.entity.Genre;

import java.util.List;

public interface GenreDAO extends GeneralBookDAO<Genre>{
    int updateNameGenre(int id,String genreName);

    Genre findByNameGenre(String nameGenre);
}
