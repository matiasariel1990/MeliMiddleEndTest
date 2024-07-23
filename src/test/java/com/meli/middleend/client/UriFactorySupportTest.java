package com.meli.middleend.client;

import com.meli.middleend.client.support.UriFactorySupport;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UriFactorySupportTest {


    private static final String URI_BASE_SEARCH_TEST = "https://api.libre.com/sites/{site}/search";
    private static final String URI_TELE_LIMIT_50_TEST = "https://api.libre.com/sites/MLB/search?q=tele&limit=50";
    private static final String URI_TELE_OFFSET_100_TEST = "https://api.libre.com/sites/MLA/search?q=tele&offset=100";
    private static final String URI_DESCRIPTION = "https://api.mercadolibre.com/items/{id}/description";
    private static final String URI_DESCRIPTION_TELE = "https://api.mercadolibre.com/items/teleid/description";


    @Test
    public void buildUrlMLBTvLimit50Test(){
        HashMap vars = new HashMap<>();
        vars.put("site", "MLB");
        HashMap params = new HashMap<>();
        params.put("q", "tele");
        params.put("limit", "50");
        String uriResult = UriFactorySupport.buildUrl(URI_BASE_SEARCH_TEST, vars, params);
        assertEquals(URI_TELE_LIMIT_50_TEST, uriResult);
    }


    @Test
    public void buildUrlMLATvoffset10050Test(){
        HashMap vars = new HashMap<>();
        vars.put("site", "MLA");
        HashMap params = new HashMap<>();
        params.put("q", "tele");
        params.put("offset", "100");
        String uriResult = UriFactorySupport.buildUrl(URI_BASE_SEARCH_TEST, vars, params);
        assertEquals(URI_TELE_OFFSET_100_TEST, uriResult);
    }

    @Test
    public void builUrlDesctiptionTest(){

        HashMap vars = new HashMap<>();
        vars.put("id", "teleid");
        String uriResult = UriFactorySupport.buildUrl(URI_DESCRIPTION, vars, null);
        assertEquals(URI_DESCRIPTION_TELE, uriResult);
    }
}
