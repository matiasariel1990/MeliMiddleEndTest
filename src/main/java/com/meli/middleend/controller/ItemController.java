package com.meli.middleend.controller;


import com.meli.middleend.dto.request.GetItemQueryRequest;
import com.meli.middleend.dto.response.ItemResponse;
import com.meli.middleend.dto.response.PageItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/item")
public interface ItemController {

    @GetMapping("/search/{site}")
    ResponseEntity<PageItemResponse> getItemBySite(@ModelAttribute GetItemQueryRequest getItemQueryRequest);

    @GetMapping("/{id}")
    ResponseEntity<ItemResponse> getItem(@PathVariable String id);

}
