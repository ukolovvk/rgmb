package com.rgmb.generator.dao;

import com.rgmb.generator.entity.Genre;

import java.util.List;

/**
 * Интерфейс для сущности - жанр книги
 * См реализацию в классе {@link com.rgmb.generator.impdao.ImpGenreDAO}
 */
public interface GenreDAO extends GeneralBookDAO<Genre>{
    int updateNameGenre(int id,String genreName);

    Genre findByNameGenre(String nameGenre);

    int addWithReturningId(Genre genre);

    int findIdByGenreName(String genreName);
}
