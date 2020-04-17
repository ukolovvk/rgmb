package com.rgmb.generator.dao;

import com.rgmb.generator.entity.Production;

public interface ProductionDAO extends GeneralMovieDAO<Production> {
    int findIdByProductionName(String productionName);

    int addWithReturningId(Production production);
}
