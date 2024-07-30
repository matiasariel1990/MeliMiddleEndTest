package com.meli.middleend.client;

import com.meli.middleend.dto.api.client.SearByQueryDto;
import com.meli.middleend.dto.api.client.response.ItemByIdResponse;
import com.meli.middleend.dto.api.client.response.ItemDescription;
import com.meli.middleend.dto.api.client.response.SearchResponse;

public interface ApiMLClient {

    SearchResponse searchByQuery(SearByQueryDto searByQueryDto);

    ItemByIdResponse getItemById(String id);

    ItemDescription getItemDescription(String id);

}
