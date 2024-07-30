package com.meli.middleend.client;

import com.meli.middleend.dto.api.client.SearByQueryDto;
import com.meli.middleend.dto.api.client.response.ItemByIdResponse;
import com.meli.middleend.dto.api.client.response.ItemDescription;
import com.meli.middleend.dto.api.client.response.SearchResponse;
import com.meli.middleend.dto.enums.SortsEnum;
import com.meli.middleend.exception.ServiceClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class ApiMLClientImplTest {

    private static final String ERROR_400_JSON_PATH = "src/test/resources/error_400_search.json";
    private static final String ERROR_API_400_TEST = "error_fallback_searchbackend";
    private static final String MESSAGE_ERROR_API_400_TEST = "Message: status_400;Error Code: error_fallback_searchbackend;Status: 400;Cause: []";
    private static final String OK_SEARCH_JSON_PATH = "src/test/resources/response_ok.json";
    private static final String ITEM_DESCRIPTION_OK_PATH = "src/test/resources/item_description_ok.json";
    private static final String ITEM_DETAIL_OK_PATH = "src/test/resources/item_By_id_response_ok.json";
    private static final String FIRST_ID_MOCK = "MLA831548882";
    private static final String URI_MOCK = "http://www.ml.com.ar/";
    private static final String SEARCH_PATH_MOCK ="sites/{site}/search";
    private static final String DESCRIPTION_PATH_MOCK = "item/{id}/description";
    private static final String ID_PATH_MOCK = "item/{id}";
    private static final String TITLE_MOCK = "El Lobo Que Queria Cambiar De Color, De Lallemand, Orianne. Editorial Los Editores De Auzou, Tapa Dura En Español, 2019";
    private static final int TOTAL_SEARCH_OK = 455;
    private static final int OFFSET_SEARCH_OK = 0;
    private static final int LIMIT_SEARCH_OK = 50;
    private static final String DESCRIPTION_MOCK = "ESTANTERIA DE PINO CON 4 ESTANTES - Altura 40cm";
    private static final String CONDITION_MOCK = "new";
    private static final BigDecimal PRICE_MOCK = BigDecimal.valueOf(19590.2);
    private static final String OK_SEARCH_ALLPARAMS_JSON_PATH = "src/test/resources/search_test_all_params_ok.json";


    @Configuration
    static class TestConfig {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

    }

    ApiMLClientImpl apiMLClientImpl;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        apiMLClientImpl = new ApiMLClientImpl(restTemplate);
        apiMLClientImpl.setSearchPath(SEARCH_PATH_MOCK);
        apiMLClientImpl.setUriBase(URI_MOCK);
        apiMLClientImpl.setGetItemDescriptionPath(DESCRIPTION_PATH_MOCK);
        apiMLClientImpl.setGetItemByIdPath(ID_PATH_MOCK);
    }

    @Test
    public void whenSearchResponseOKReturn() throws IOException {


        SearByQueryDto queryDto = new SearByQueryDto();
        queryDto.setQuery("tele");

        String searchResponsemock = new String(Files.readAllBytes(Paths.get(OK_SEARCH_JSON_PATH)));

        mockServer.expect(MockRestRequestMatchers.anything())
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(searchResponsemock));

        SearchResponse searchResponsem = apiMLClientImpl.searchByQuery(queryDto);
        assertEquals(TOTAL_SEARCH_OK, searchResponsem.getPaging().getTotal());
        assertEquals(OFFSET_SEARCH_OK, searchResponsem.getPaging().getOffset());
        assertEquals(FIRST_ID_MOCK, searchResponsem.getResults().get(0).getId());
        assertEquals(LIMIT_SEARCH_OK, searchResponsem.getPaging().getLimit());
        assertEquals(LIMIT_SEARCH_OK, searchResponsem.getResults().size());
    }


    @Test
    public void searchWhitAllArgsQueryResponseOKReturn() throws IOException {


        SearByQueryDto queryDto = new SearByQueryDto();

        queryDto.setQuery("tele");
        queryDto.setSite("MLB");
        queryDto.setOffset("3");
        queryDto.setLimit("5");
        queryDto.setSort(SortsEnum.PRICE_DESC);

        String parthSpected = "sites/MLB/search?q=tele&offset=3&limit=5&sort=price_desc";

        String searchResponsemock = new String(Files.readAllBytes(Paths.get(OK_SEARCH_ALLPARAMS_JSON_PATH)));

        mockServer.expect(MockRestRequestMatchers.requestTo(URI_MOCK + parthSpected))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(searchResponsemock));

        SearchResponse searchResponsem = apiMLClientImpl.searchByQuery(queryDto);
        assertEquals(3, searchResponsem.getPaging().getOffset());
        assertEquals(5, searchResponsem.getPaging().getLimit());
    }

    @Test
    public void whenRestClientResponseErrorThenExpectedServiceExceptionTest() throws IOException {

        SearByQueryDto queryDto = new SearByQueryDto();
        queryDto.setQuery("tele");

        String errorJson = new String(Files.readAllBytes(Paths.get(ERROR_400_JSON_PATH)));

        mockServer.expect(MockRestRequestMatchers.anything())
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(errorJson));

        ServiceClientException se = assertThrows(ServiceClientException.class, () -> {
            apiMLClientImpl.searchByQuery(queryDto);
        });

        assertEquals(ERROR_API_400_TEST, se.getError());
        assertEquals(MESSAGE_ERROR_API_400_TEST, se.getMessage());
        assertEquals(400, se.getStatus());

    }

    @Test
    public void itemDescriptionOKTest() throws IOException {

        String itemDescriptionJson = new String(Files.readAllBytes(Paths.get(ITEM_DESCRIPTION_OK_PATH)));

        mockServer.expect(MockRestRequestMatchers.requestTo(URI_MOCK+ "item/unId/description"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(itemDescriptionJson));

        ItemDescription itemDescription = apiMLClientImpl.getItemDescription("unId");

        assertEquals(DESCRIPTION_MOCK, itemDescription.getPlain_text());
    }


    @Test
    public void ifServerResponseErrorWhenCallItemDescriptionExpectedAnExceptionTest() throws IOException {

        String error400Json = new String(Files.readAllBytes(Paths.get(ERROR_400_JSON_PATH)));

        mockServer.expect(MockRestRequestMatchers.requestTo(URI_MOCK+ "item/unId/description"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(error400Json));

        ServiceClientException se = assertThrows(ServiceClientException.class, () -> {
            apiMLClientImpl.getItemDescription("unId");
        });

        assertEquals(ERROR_API_400_TEST, se.getError());
        assertEquals(MESSAGE_ERROR_API_400_TEST, se.getMessage());
        assertEquals(400, se.getStatus());
    }


    @Test
    public void itembyIdOKTest() throws IOException {

        String itembyId = new String(Files.readAllBytes(Paths.get(ITEM_DETAIL_OK_PATH)));

        mockServer.expect(MockRestRequestMatchers.requestTo(URI_MOCK+ "item/unId"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(itembyId));

        ItemByIdResponse itemByIdResponse = apiMLClientImpl.getItemById("unId");


        assertEquals("id", itemByIdResponse.getId());
        assertEquals(CONDITION_MOCK, itemByIdResponse.getCondition());
        assertEquals(PRICE_MOCK, itemByIdResponse.getPrice());
        assertEquals(TITLE_MOCK, itemByIdResponse.getTitle());
        assertEquals(false, itemByIdResponse.getShipping().isFree_shipping());
    }



}
