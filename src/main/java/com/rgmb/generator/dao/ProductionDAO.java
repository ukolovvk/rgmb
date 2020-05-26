package com.rgmb.generator.dao;

import com.rgmb.generator.entity.Production;

/**
 * Интерфейс для сущности - кинокомпания
 * См реализацию в классе {@link com.rgmb.generator.impdao.ImpProductionDAO}
 */
public interface ProductionDAO extends GeneralMovieDAO<Production> {
    int findIdByProductionName(String productionName);

    int addWithReturningId(Production production);
}
