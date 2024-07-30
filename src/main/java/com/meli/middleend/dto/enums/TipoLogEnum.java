package com.meli.middleend.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoLogEnum {
    LOG_APP(1, "log-app"),
    LOG_BACK_APP(2, "log-back-app");

    int tipoLog;
    String description;
}
