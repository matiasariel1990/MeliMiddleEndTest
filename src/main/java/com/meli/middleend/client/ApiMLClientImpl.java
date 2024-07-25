package com.meli.middleend.client;

import com.meli.middleend.client.support.UriFactorySupport;
import com.meli.middleend.dto.api.client.SearByQueryDto;
import com.meli.middleend.dto.api.client.response.ResponseApiError;
import com.meli.middleend.dto.api.client.response.SearchResponse;
import com.meli.middleend.exception.ServiceClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class ApiMLClientImpl implements ApiMLClient{

    private static final String QUERY_KEY = "q";
    private static final String OFFSET_KEY = "offset";
    private static final String LIMIT_KEY = "limit";
    private static final String SITE_KEY = "site";

    String URI = "https://api.mercadolibre.com/sites/{site}/search";

    private final RestTemplate restTemplate;


    @Autowired
    public ApiMLClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }



    @Override
    public SearchResponse searchByQuery(SearByQueryDto searByQueryDto) {
        HashMap<String, String> params = new HashMap<>();

        params.put(QUERY_KEY, searByQueryDto.getQuery());

        if(searByQueryDto.getSort() != null){
            params.put(searByQueryDto.getSort().getId(),
                    searByQueryDto.getSort().getName());
        }
        if(searByQueryDto.getOffset() != null){
            params.put(OFFSET_KEY,
                    searByQueryDto.getOffset());
        }
        if(searByQueryDto.getLimit() != null){
            params.put(LIMIT_KEY,
                    searByQueryDto.getLimit());
        }

        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put(SITE_KEY, searByQueryDto.getSite());

        String url = UriFactorySupport.buildUrl(URI, uriVariables, params);

        try{
            ResponseEntity<SearchResponse> responseEntity =
                    restTemplate.getForEntity(url, SearchResponse.class);

            return responseEntity.getBody();
        }catch (HttpClientErrorException clientErrorException){
            ResponseApiError responseApiError = clientErrorException.getResponseBodyAs(ResponseApiError.class);
            throw new ServiceClientException(responseApiError);
        }

    }
}
