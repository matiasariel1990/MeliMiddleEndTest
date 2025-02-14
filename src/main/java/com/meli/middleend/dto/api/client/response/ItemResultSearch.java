package com.meli.middleend.dto.api.client.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor

public class ItemResultSearch {
    private String id;
    private String title;
    private String condition;
    private String category_id;
    private String currency_id;
    private BigDecimal price;
    private String thumbnail;
    private Shipping shipping;
}
