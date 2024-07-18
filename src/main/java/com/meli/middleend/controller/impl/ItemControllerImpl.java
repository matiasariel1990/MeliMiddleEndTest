package com.meli.middleend.controller.impl;

import com.meli.middleend.controller.ItemController;
import com.meli.middleend.dto.ItemDeteail;
import com.meli.middleend.dto.QueryDto;
import com.meli.middleend.dto.SortsEnum;
import com.meli.middleend.dto.response.ItemResponse;
import com.meli.middleend.dto.response.PageItemResponse;
import com.meli.middleend.service.ItemService;
import com.meli.middleend.utils.MockConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemControllerImpl implements ItemController {

    @Autowired
    ItemService itemService;

    @Override
    public ResponseEntity<PageItemResponse> getItemBySite(String site, String query, int offset, int limit, String sortBy, String sortOrder) {
        QueryDto queryDto = QueryDto.builder()
                .query(query)
                .offset(offset)
                .limit(limit)
                .build();
        return new ResponseEntity<>(itemService.getItemsByQuery(queryDto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ItemResponse> getItem(String id) {

        return new ResponseEntity<>(itemService.getItemById(id), HttpStatus.OK);
    }
}
