package com.meli.middleend.utils;

import com.meli.middleend.dto.Item;
import com.meli.middleend.dto.Price;
import com.meli.middleend.dto.QueryDto;
import com.meli.middleend.dto.api.client.SearByQueryDto;
import com.meli.middleend.dto.api.client.response.ItemResultSearch;
import com.meli.middleend.dto.response.ItemResponse;

import java.math.BigDecimal;

public class Mapper {

    public static SearByQueryDto mapToSearByQueryDto(QueryDto queryDto){
        SearByQueryDto searByQueryDto = new SearByQueryDto();
        searByQueryDto.setQuery(queryDto.getQuery());
        searByQueryDto.setLimit(String.valueOf(queryDto.getLimit()));
        searByQueryDto.setSort(queryDto.getSortEnum());
        searByQueryDto.setSort(queryDto.getSortEnum());
        searByQueryDto.setSite(queryDto.getSiteEnum().getSiteCode());
        if(queryDto.getOffset() != 0){
            searByQueryDto.setOffset(String.valueOf(queryDto.getOffset()));
        }
        return searByQueryDto;
    }


    public static Item mapToItem(ItemResultSearch itemResultSearch){
        Item item = new Item();
        item.setId(itemResultSearch.getId());
        item.setTitle(itemResultSearch.getTitle());
        item.setPrice(mapToPrice(itemResultSearch.getPrice(), itemResultSearch.getCurrency_id()));
        item.setFree_shipping(itemResultSearch.getShipping().isFree_shipping());
        item.setCondition(itemResultSearch.getCondition());
        item.setPicture(itemResultSearch.getThumbnail());
        return item;
    }

    public static Price mapToPrice(BigDecimal ammount, String currencyId) {
        return Price.builder()
                .decimals(ammount.scale())
                .amount(ammount.movePointRight(ammount.scale()).longValueExact())
                .currency(currencyId)
                .build();
    }
}
