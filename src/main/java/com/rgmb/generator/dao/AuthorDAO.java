package com.rgmb.generator.dao;

import com.rgmb.generator.entity.Author;

/**
 * Интерфейс сущности - автор книги
 * См. реализацию в классе {@link com.rgmb.generator.impdao.ImpAuthorDAO}
 */

public interface AuthorDAO extends GeneralBookDAO<Author>{

    int updateNameById(int id,String name);

    Author findByName(String name);

    int addWithReturningId(Author author);

    int findIdByAuthorName(String authorName);
}
