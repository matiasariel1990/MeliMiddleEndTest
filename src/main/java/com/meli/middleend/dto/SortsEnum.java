package com.meli.middleend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortsEnum {

    PRICE_ASC("price_asc", "Menor precio."),
    PRICE_DESC("price_desc", "Mayor precio");

    String id;
    String name;
}
