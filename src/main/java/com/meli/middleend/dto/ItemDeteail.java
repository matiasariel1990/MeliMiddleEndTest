package com.meli.middleend.dto;

import lombok.*;

@Getter
@Setter
public class ItemDeteail extends Item {

    public ItemDeteail(){
        super();
    }
    int sold_quantity;
    String description;
}
