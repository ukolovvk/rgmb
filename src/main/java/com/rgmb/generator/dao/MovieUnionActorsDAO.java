package com.rgmb.generator.dao;

import com.rgmb.generator.entity.Actor;
import com.rgmb.generator.entity.Movie;

public interface MovieUnionActorsDAO {
    int add(int movieID, int actorID);

    int delete(int movieID);
}
