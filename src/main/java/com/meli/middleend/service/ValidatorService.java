package com.meli.middleend.service;

import com.meli.middleend.dto.QueryDto;
import com.meli.middleend.dto.SiteEnum;
import com.meli.middleend.dto.SortsEnum;

public interface ValidatorService {

    SiteEnum validarSite(String site);

    String validarQuery(String query);

    SortsEnum validarSort(String sortBy);

    int validarOffset(int offset);

    int validarLimit(int limit);

    String validarId(String id);

}
