package com.meli.middleend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonFormatter {

    public static String formatJson(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        Object jsonResponse;
        try {
            jsonResponse = mapper.readValue(jsonString, Object.class);
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            return writer.writeValueAsString(jsonResponse);
        } catch (Exception e) {
            return jsonString;
        }
    }
}
