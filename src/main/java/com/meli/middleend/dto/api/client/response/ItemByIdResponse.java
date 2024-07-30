package com.meli.middleend.dto.api.client.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ItemByIdResponse extends ItemResultSearch{
    List<Picture> pictures;
    String seller_id;
    int initial_quantity;
}
