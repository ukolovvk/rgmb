package com.rgmb.generator.dao;

import com.rgmb.generator.entity.GameGenre;

/**
 * Интерфейс для сущности - жанр игры
 * См реализацию в классе {@link com.rgmb.generator.impdao.ImpGameGenreDAO}
 */
public interface GameGenreDAO extends GeneralGameDAO<GameGenre> {
    int findIdByGameGenreName(String  gameGenreName);

    int addWithReturningId(GameGenre genre);

    int updateById(int id, GameGenre gameGenre);
}
