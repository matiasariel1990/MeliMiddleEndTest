package com.meli.middleend.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
    String id;
    String title;
    Price price;
    String picture;
    String condition;
    boolean free_shipping;
}
