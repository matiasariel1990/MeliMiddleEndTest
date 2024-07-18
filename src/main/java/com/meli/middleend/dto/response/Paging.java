package com.meli.middleend.dto.response;

import com.meli.middleend.dto.Item;
import lombok.Data;

import java.util.List;

@Data
public class Paging {
    int total;
    int offset;
    int limit;
}
