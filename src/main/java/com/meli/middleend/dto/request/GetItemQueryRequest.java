package com.meli.middleend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetItemQueryRequest {
    String site;
    String query;
    int offset = 0;
    int limit = 20;
    String sortBy;
}
