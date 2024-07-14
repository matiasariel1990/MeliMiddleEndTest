package com.meli.middleend.dto;

import lombok.Data;

@Data
public class ItemDeteail extends Item {
    int sold_quantity;
    String description;
}
