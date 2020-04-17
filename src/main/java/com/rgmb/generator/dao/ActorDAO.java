package com.rgmb.generator.dao;

import com.rgmb.generator.entity.Actor;


public interface ActorDAO extends GeneralMovieDAO<Actor> {
    int findIdByActorName(String  actorName);

    int addWithReturningId(Actor actor);
}
