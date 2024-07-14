package com.meli.middleend.dto.response;

import com.meli.middleend.dto.Item;
import lombok.Data;

import java.util.List;

@Data
public class Paging {
    int number;
    int offset;
    int limit;
    List<String> categories;
    List<Item> items;
}
