package com.meli.middleend.dto.api.client.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Picture {
    String secure_url;

    @JsonCreator
    public Picture(@JsonProperty("secure_url") String secure_url) {
        this.secure_url = secure_url;
    }
}
