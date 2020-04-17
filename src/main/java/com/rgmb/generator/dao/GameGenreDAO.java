package com.rgmb.generator.dao;

import com.rgmb.generator.entity.GameGenre;

public interface GameGenreDAO extends GeneralGameDAO<GameGenre> {
    int findIdByGameGenreName(String  gameGenreName);

    int addWithReturningId(GameGenre genre);
}
