package com.meli.middleend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Price {
    String currency;
    long amount;
    int decimals;
}

