package com.meli.middleend.service;

import com.meli.middleend.dto.QueryDto;
import com.meli.middleend.dto.response.ItemResponse;
import com.meli.middleend.dto.response.PageItemResponse;

public interface ItemService {

    ItemResponse getItemById(String id);

    PageItemResponse getItemsByQuery(QueryDto query);

}
