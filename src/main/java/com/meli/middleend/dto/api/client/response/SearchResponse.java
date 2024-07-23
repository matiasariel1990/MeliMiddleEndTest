package com.meli.middleend.dto.api.client.response;

import com.meli.middleend.dto.response.Paging;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchResponse {
    Paging paging;
    List<ItemResultSearch> results;
}
