package com.rgmb.generator.dao;

import com.rgmb.generator.entity.GameCompany;

public interface CompanyDAO extends GeneralGameDAO<GameCompany>{
    int findIdByGameCompanyName(String  gameCompanyName);

    int addWithReturningId(GameCompany company);
}
