package com.meli.middleend.client;

import com.meli.middleend.client.support.UriFactorySupport;
import com.meli.middleend.dto.api.client.SearByQueryDto;
import com.meli.middleend.dto.api.client.response.ItemByIdResponse;
import com.meli.middleend.dto.api.client.response.ItemDescription;
import com.meli.middleend.dto.api.client.response.ResponseApiError;
import com.meli.middleend.dto.api.client.response.SearchResponse;
import com.meli.middleend.exception.ServiceClientException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;

@Service
@Setter
public class ApiMLClientImpl implements ApiMLClient{

    private static final String QUERY_KEY = "q";
    private static final String OFFSET_KEY = "offset";
    private static final String LIMIT_KEY = "limit";
    private static final String SITE_KEY = "site";
    private static final String SORT_KEY = "sort";

    @Value("${api.mercadolibre.uri.base}")
    String uriBase;

    @Value("${api.mercadolibre.path.search}")
    String searchPath;

    @Value("${api.mercadolibre.path.get.item}")
    String getItemByIdPath;

    @Value("${api.mercadolibre.path.get.item.description}")
    String getItemDescriptionPath;

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
            params.put(SORT_KEY, searByQueryDto.getSort().getId());
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

        String url = UriFactorySupport.buildUrl(uriBase.concat(searchPath), uriVariables, params);

        try{
            ResponseEntity<SearchResponse> responseEntity =
                    restTemplate.getForEntity(url, SearchResponse.class);

            return responseEntity.getBody();
        }catch (HttpClientErrorException clientErrorException){
            ResponseApiError responseApiError = clientErrorException.getResponseBodyAs(ResponseApiError.class);
            throw new ServiceClientException(responseApiError);
        }

    }

    @Override
    public ItemByIdResponse getItemById(String id) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uriBase.concat(getItemByIdPath));
        String url = uriBuilder.buildAndExpand(id).toUriString();
        try{
            ResponseEntity<ItemByIdResponse> responseEntity =
                    restTemplate.getForEntity(url, ItemByIdResponse.class);

            return responseEntity.getBody();
        }catch (HttpClientErrorException clientErrorException){
            ResponseApiError responseApiError = clientErrorException.getResponseBodyAs(ResponseApiError.class);
            throw new ServiceClientException(responseApiError);
        }
    }

    @Override
    public ItemDescription getItemDescription(String id) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uriBase.concat(getItemDescriptionPath));
        String url = uriBuilder.buildAndExpand(id).toUriString();
        try{
            ResponseEntity<ItemDescription> responseEntity =
                    restTemplate.getForEntity(url, ItemDescription.class);
            return responseEntity.getBody();
        }catch (HttpClientErrorException clientErrorException){
            ResponseApiError responseApiError = clientErrorException.getResponseBodyAs(ResponseApiError.class);
            throw new ServiceClientException(responseApiError);
        }
    }
}
