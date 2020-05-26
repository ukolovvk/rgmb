package com.rgmb.generator.dao;

import com.rgmb.generator.entity.GameCompany;

/**
 * Интерфейс для сущности - компания
     * См реализацию в классе {@link com.rgmb.generator.impdao.ImpCompanyDAO}
 */
public interface CompanyDAO extends GeneralGameDAO<GameCompany>{
    int findIdByGameCompanyName(String  gameCompanyName);

    int addWithReturningId(GameCompany company);

    int updateById(int id, GameCompany gameCompany);
}
