package com.rgmb.generator.dao;

import com.rgmb.generator.entity.Actor;

/**
 * Интерфейс для сущности - актер
 * См реализацию в классе {@link com.rgmb.generator.impdao.ImpActorDAO}
 */

public interface ActorDAO extends GeneralMovieDAO<Actor> {

    int findIdByActorName(String  actorName);

    int addWithReturningId(Actor actor);
}
