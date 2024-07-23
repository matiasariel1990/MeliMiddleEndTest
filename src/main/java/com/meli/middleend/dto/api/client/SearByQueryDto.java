package com.meli.middleend.dto.api.client;

import com.meli.middleend.dto.SortsEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SearByQueryDto {
    String site;
    String query;
    SortsEnum sort;
    String limit;
    String offset;
}
