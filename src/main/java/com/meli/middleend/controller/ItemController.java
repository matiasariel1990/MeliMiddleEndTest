package com.meli.middleend.controller;


import com.meli.middleend.dto.ItemDeteail;
import com.meli.middleend.dto.response.ItemResponse;
import com.meli.middleend.dto.response.PageItemResponse;
import com.meli.middleend.dto.response.Paging;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/item")
public interface ItemController {

    @GetMapping("/search/{site}")
    ResponseEntity<PageItemResponse> getItemBySite(@PathVariable() String site,
                                                   @RequestParam(required = true) String query,
                                                   @RequestParam(defaultValue = "0") int offset,
                                                   @RequestParam(defaultValue = "10") int limit,
                                                   @RequestParam(required = false) String sortBy,
                                                   @RequestParam(required = false) String sortOrder);

    @GetMapping("/{id}")
    ResponseEntity<ItemResponse> getItem(@PathVariable String id);

}
