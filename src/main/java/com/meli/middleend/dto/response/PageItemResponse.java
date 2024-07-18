package com.meli.middleend.dto.response;

import com.meli.middleend.dto.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
public class PageItemResponse {
    Paging paging;
    Set<String> categories;
    List<Item> items;

}
