package com.meli.middleend.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SiteEnum {
    MLA("MLA", "MercadoLibreArg"),
    MLB("MLB", "MercadoLibreBr"),
    MLM("MLM", "MercadoLibreMex");

    String siteCode;
    String descripcion;
}
