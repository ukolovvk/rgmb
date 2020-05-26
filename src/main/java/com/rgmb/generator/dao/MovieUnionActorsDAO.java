package com.rgmb.generator.dao;

import com.rgmb.generator.entity.Actor;
import com.rgmb.generator.entity.Movie;

/**
 * Интерфейс для класса, реализующщего логику работы с объединенной таблицей Фильмов и актеров
 * См. реализацию в классе {@link com.rgmb.generator.impdao.ImpMovieUnionActorsDAO}
 */
public interface MovieUnionActorsDAO {
    int add(int movieID, int actorID);

    int delete(int movieID);
}
