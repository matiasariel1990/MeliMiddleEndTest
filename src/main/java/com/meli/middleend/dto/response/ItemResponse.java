package com.meli.middleend.dto.response;

import com.meli.middleend.dto.Author;
import com.meli.middleend.dto.ItemDeteail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ItemResponse {
    Author author;
    ItemDeteail item;
}
