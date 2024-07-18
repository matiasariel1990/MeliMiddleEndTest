package com.meli.middleend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class Author {
    String name;
    String lastName;
}
