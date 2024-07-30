package com.meli.middleend.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonFormmatterTest {


    private static final String JSON_OBJET_TEST = "{\"id\": \"MLA1718304066\", \"title\": \"Joystick Victrix Pro Fs-12 Es. Fight Stick Para Ps4_ps5_pc\", \"price\": {\"currency\": \"ARS\", \"amount\": 2516709, \"decimals\": 0}, \"picture\": \"http://http2.mlstatic.com/D_679318-MLA76049386090_052024-I.jpg\", \"condition\": \"new\", \"free_shipping\": true}";
    private static final String JSON_EXPECTED = "{\n" +
            "  \"id\" : \"MLA1718304066\",\n" +
            "  \"title\" : \"Joystick Victrix Pro Fs-12 Es. Fight Stick Para Ps4_ps5_pc\",\n" +
            "  \"price\" : {\n" +
            "    \"currency\" : \"ARS\",\n" +
            "    \"amount\" : 2516709,\n" +
            "    \"decimals\" : 0\n" +
            "  },\n" +
            "  \"picture\" : \"http://http2.mlstatic.com/D_679318-MLA76049386090_052024-I.jpg\",\n" +
            "  \"condition\" : \"new\",\n" +
            "  \"free_shipping\" : true\n" +
            "}";

    @Test
    public void jsonOKTest(){

        assertEquals(JSON_EXPECTED.replaceAll("\\s+", ""),
                JsonFormatter.formatJson(JSON_OBJET_TEST).replaceAll("\\s+", ""));
    }

    @Test
    public void noJsonReturnSameStringTest(){
        String noJson = "{\"id\":\"2222";
        assertEquals(noJson.replaceAll("\\s+", ""),
                JsonFormatter.formatJson(noJson).replaceAll("\\s+", ""));
    }

}
