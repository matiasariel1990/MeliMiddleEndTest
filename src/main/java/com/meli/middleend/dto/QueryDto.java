package com.meli.middleend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QueryDto {

    String query;
    SortsEnum sortEnum;
    int offset;
    int limit;
}
