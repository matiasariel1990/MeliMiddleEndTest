package com.meli.middleend.client;

import com.meli.middleend.dto.api.client.SearByQueryDto;
import com.meli.middleend.dto.api.client.response.ResponseApiError;
import com.meli.middleend.dto.api.client.response.SearchResponse;
import com.meli.middleend.exception.ServiceClientException;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import static javax.management.Query.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ApiMLClientImplTest {

    private static final String ERROR_400_JSON_PATH = "src/test/resources/error_400_search.json";
    private static final String ERROR_API_400_TEST = "error_fallback_searchbackend";
    private static final String MESSAGE_ERROR_API_400_TEST = "Message: status_400;Error Code: error_fallback_searchbackend;Status: 400;Cause: []";
    private static final String OK_SEARCH_JSON_PATH = "src/test/resources/response_ok.json";
    private static final String FIRST_ID_MOCK = "MLA831548882";
    private static final int TOTAL_SEARCH_OK = 455;
    private static final int OFFSET_SEARCH_OK = 0;
    private static final int LIMIT_SEARCH_OK = 50;

    @Configuration
    static class TestConfig {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }


    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void whenSearchResponseOKReturn() throws IOException {

        ApiMLClientImpl apiMLClientImpl = new ApiMLClientImpl(restTemplate);
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
    public void whenRestClientResponseErrorThenExpectedServiceExceptionTest() throws IOException {
        ApiMLClientImpl apiMLClientImpl = new ApiMLClientImpl(restTemplate);

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



}
