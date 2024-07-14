package com.meli.middleend.dto;


import lombok.Data;

@Data
public class Item {
    String id;
    String title;
    Price price;
    String picture;
    String condition;
    boolean free_shipping;
}
