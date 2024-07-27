package com.meli.middleend.dto.api.client;

import com.meli.middleend.dto.enums.SortsEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearByQueryDto {
    String site;
    String query;
    SortsEnum sort;
    String limit;
    String offset;
}
