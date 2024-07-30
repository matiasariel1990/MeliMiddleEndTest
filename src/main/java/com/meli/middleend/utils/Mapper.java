package com.meli.middleend.utils;

import com.meli.middleend.dto.Item;
import com.meli.middleend.dto.ItemDeteail;
import com.meli.middleend.dto.Price;
import com.meli.middleend.dto.QueryDto;
import com.meli.middleend.dto.api.client.SearByQueryDto;
import com.meli.middleend.dto.api.client.response.ItemByIdResponse;
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

    public static ItemDeteail mapToItemResponse(ItemByIdResponse itemByIdResponse) {
        ItemDeteail itemDeteail = new ItemDeteail();
        itemDeteail.setId(itemByIdResponse.getId());
        itemDeteail.setTitle(itemByIdResponse.getTitle());
        itemDeteail.setPrice(mapToPrice(itemByIdResponse.getPrice(), itemByIdResponse.getCurrency_id()));
        itemDeteail.setCondition(itemByIdResponse.getCondition());
        itemDeteail.setSold_quantity(itemByIdResponse.getInitial_quantity());
        itemDeteail.setPicture(itemByIdResponse.getPictures().get(0).getSecure_url());
        return itemDeteail;
    }
}
