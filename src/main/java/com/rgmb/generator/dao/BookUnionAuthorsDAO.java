package com.rgmb.generator.dao;

/**
 * Интерфейс для класса, реализующщего логику работы с объединенной таблицей книг и авторов
 * См. реализацию в классе {@link com.rgmb.generator.impdao.ImpBookUnionAuthorsDAO}
 */
public interface BookUnionAuthorsDAO {
        int add(int bookID, int authorID);

        int delete(int bookID);
}
