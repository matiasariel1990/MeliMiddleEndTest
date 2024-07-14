package com.meli.middleend.dto;

import lombok.Data;

@Data
public class ItemDeteail extends Item {
    String condition;
    int sold_quantity;
    String description;
}
