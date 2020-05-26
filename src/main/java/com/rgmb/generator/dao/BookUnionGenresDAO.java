package com.rgmb.generator.dao;

/**
 * Интерфейс для класса, реализующего логику работы с объединенной таблице книг и жанров
 * См. реализацию в классе {@link com.rgmb.generator.impdao.ImpBookUnionGenresDAO}
 */
public interface BookUnionGenresDAO {

    int add(int bookID, int genreID);

    int delete(int bookID);

}
