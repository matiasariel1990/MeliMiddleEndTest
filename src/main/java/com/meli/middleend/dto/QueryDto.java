package com.meli.middleend.dto;

import com.meli.middleend.dto.enums.SiteEnum;
import com.meli.middleend.dto.enums.SortsEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QueryDto {
    SiteEnum siteEnum;
    String query;
    SortsEnum sortEnum;
    int offset;
    int limit;
}
