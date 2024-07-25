package com.meli.middleend.service.impl;

import com.meli.middleend.client.ApiMLClient;
import com.meli.middleend.dto.Item;
import com.meli.middleend.dto.QueryDto;
import com.meli.middleend.dto.api.client.SearByQueryDto;
import com.meli.middleend.dto.api.client.response.SearchResponse;
import com.meli.middleend.dto.response.ItemResponse;
import com.meli.middleend.dto.response.PageItemResponse;
import com.meli.middleend.service.ItemService;
import com.meli.middleend.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ApiMLClient apiMLClient;

    @Override
    public ItemResponse getItemById(String id) {


        return null;
    }

    @Override
    public PageItemResponse getItemsByQuery(QueryDto query) {

        SearByQueryDto searByQueryDto = Mapper.mapToSearByQueryDto(query);

        SearchResponse searchResponse = apiMLClient.searchByQuery(searByQueryDto);

        PageItemResponse pageItemResponse = PageItemResponse.builder().build();
        pageItemResponse.setPaging(searchResponse.getPaging());
        HashSet<String> categories = new HashSet<>();
        List<Item> itemList = searchResponse.getResults()
                .stream().peek(item -> categories.add(item.getCategory_id())).map(Mapper::mapToItem).toList();
        pageItemResponse.setItems(itemList);
        pageItemResponse.setCategories(categories);

        return pageItemResponse;
    }
}
