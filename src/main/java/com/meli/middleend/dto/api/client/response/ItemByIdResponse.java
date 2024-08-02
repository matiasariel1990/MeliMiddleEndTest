package com.meli.middleend.dto.api.client.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemByIdResponse extends ItemResultSearch{
    List<Picture> pictures;
    String seller_id;
    int initial_quantity;
}
