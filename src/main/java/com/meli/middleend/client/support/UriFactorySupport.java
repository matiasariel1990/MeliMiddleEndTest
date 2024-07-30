package com.meli.middleend.client.support;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

public class UriFactorySupport {

    public static String buildUrl(String base, HashMap<String, String> vars, HashMap<String, String> params){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(base);
        if(params != null){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                uriBuilder.queryParam(entry.getKey(), entry.getValue());
            }
        }
        return uriBuilder.buildAndExpand(vars).toUriString();
    }

}
