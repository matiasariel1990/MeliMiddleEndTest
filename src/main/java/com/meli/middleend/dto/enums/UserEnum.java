package com.meli.middleend.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum UserEnum {
    USER_CLIENT("USER_CLIENT", "ROLE_CLIENT"),
    USER_MOCK("USER_MOCK_CLIENT", "ROLE_MOCK_CLIENT");

    String userName;
    String role;
}
