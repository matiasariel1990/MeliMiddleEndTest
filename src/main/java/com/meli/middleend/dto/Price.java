package com.meli.middleend.dto;

import lombok.Data;

@Data
public class Price {
    String currency;
    int amount;
    int decimals;
}

