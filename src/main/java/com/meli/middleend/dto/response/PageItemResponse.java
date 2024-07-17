package com.meli.middleend.dto.response;

import com.meli.middleend.dto.Item;

import java.util.Set;

public class PageItemResponse {
    Paging paging;
    Set<String> categories;
    Set<Item> items;
}
