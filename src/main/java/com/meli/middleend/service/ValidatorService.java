package com.meli.middleend.service;

import com.meli.middleend.dto.QueryDto;
import com.meli.middleend.dto.enums.SiteEnum;
import com.meli.middleend.dto.enums.SortsEnum;
import com.meli.middleend.dto.request.GetItemQueryRequest;

public interface ValidatorService {

    QueryDto validarQueryParams(GetItemQueryRequest getItemQueryRequest);

    SiteEnum validarSite(String site);

    String validarQuery(String query);

    SortsEnum validarSort(String sortBy);

    int validarOffset(int offset);

    int validarLimit(int limit);

    String validarId(String id);

}
