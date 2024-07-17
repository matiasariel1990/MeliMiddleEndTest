package com.meli.middleend.dto.response;

import com.meli.middleend.dto.Author;
import com.meli.middleend.dto.ItemDeteail;
import lombok.Data;

@Data
public class ItemResponse {
    Author author;
    ItemDeteail item;
}
