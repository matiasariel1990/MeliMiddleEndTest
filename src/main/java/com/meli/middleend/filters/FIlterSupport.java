package com.meli.middleend.filters;

import static com.meli.middleend.utils.StringConstants.DOC_START_PATH;
import static com.meli.middleend.utils.StringConstants.SWAGGER_START_PATH;

public class FIlterSupport {


    public static boolean isFromSwagger(String requestURI) {
        return  requestURI.startsWith(SWAGGER_START_PATH) ||
                requestURI.startsWith(DOC_START_PATH);
    }
}
