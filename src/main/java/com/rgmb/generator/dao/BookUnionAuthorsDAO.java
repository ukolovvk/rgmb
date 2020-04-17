package com.rgmb.generator.dao;

public interface BookUnionAuthorsDAO {
        int add(int bookID, int authorID);

        int delete(int bookID);
}
