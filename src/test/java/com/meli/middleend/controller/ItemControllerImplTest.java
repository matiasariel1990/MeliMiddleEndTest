package com.meli.middleend.controller;


import com.google.gson.Gson;
import com.meli.middleend.controller.impl.ItemControllerImpl;
import com.meli.middleend.dto.ItemDeteail;
import com.meli.middleend.dto.QueryDto;
import com.meli.middleend.dto.api.client.SearByQueryDto;
import com.meli.middleend.dto.enums.SiteEnum;
import com.meli.middleend.dto.enums.SortsEnum;
import com.meli.middleend.dto.request.GetItemQueryRequest;
import com.meli.middleend.dto.response.ItemResponse;
import com.meli.middleend.dto.response.PageItemResponse;
import com.meli.middleend.exception.ServiceException;
import com.meli.middleend.exception.ValidationException;
import com.meli.middleend.factory.FactoryItem;
import com.meli.middleend.service.ItemService;
import com.meli.middleend.service.ValidatorService;
import com.meli.middleend.utils.MockConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemControllerImplTest {

    private static final String ID_MOCK = "unId";
    private static final String QUERY_TEST = "query";
    private static final String ERROR_MESSAGE_MOCK = "A message error.";

    private static final String PAGE_ITEM_RESPONSE_JSON_PATH = "src/test/resources/page_item_response_ok.json";



    @InjectMocks
    ItemControllerImpl itemController;

    @Mock
    ItemService itemService;

    @Mock
    ValidatorService validatorService;

    GetItemQueryRequest getItemQueryRequest;

    @BeforeEach
    public void setUp(){
        getItemQueryRequest = new GetItemQueryRequest();
        getItemQueryRequest.setSite("MLA");
        getItemQueryRequest.setQuery(QUERY_TEST);
        getItemQueryRequest.setLimit(0);
        getItemQueryRequest.setOffset(10);

        itemController.setValidatorService(validatorService);
    }

    @Test
    public void whenItemServiceResponsesOkExpected200AndEntityItemResponseTest(){

        when(validatorService.validarId(ID_MOCK)).thenReturn(ID_MOCK);

        ItemDeteail itemDeteail = FactoryItem.getMockItemDetail(ID_MOCK);

        when(itemService.getItemById(ID_MOCK)).thenReturn(
                ItemResponse.builder()
                        .item(itemDeteail)
                    .build());

        ResponseEntity<ItemResponse> entityResponse = itemController.getItem(ID_MOCK);

        assertEquals(HttpStatus.OK, entityResponse.getStatusCode());

        assertEquals(ID_MOCK, entityResponse.getBody().getItem().getId());
        assertEquals(MockConstants.MOCK_DESCRIPTION, entityResponse.getBody().getItem().getDescription());
    }



    @Test void whenItemServicThrowsAnExceptionExpectedAnExceptionTest(){
        when(validatorService.validarId(ID_MOCK)).thenReturn(ID_MOCK);
        when(itemService.getItemById(ID_MOCK)).thenThrow(new ServiceException(ERROR_MESSAGE_MOCK));

        assertThrows(ServiceException.class, () ->itemController.getItem(ID_MOCK));

    }

    @Test
    public void whenValidatorTrhowsAnExceptionExpectedAnExceptionTest(){
        when(validatorService.validarId(ID_MOCK)).thenThrow(new ValidationException(""));

        assertThrows(ValidationException.class, () ->itemController.getItem(ID_MOCK));
    }

    @Test
    public void whenValidateHeadersThrownsAnExceptionExpectedAnExceptionTest(){
        when(validatorService.validarQueryParams(any())).thenThrow(new ValidationException(""));
        assertThrows(ValidationException.class, () ->itemController.getItemBySite(getItemQueryRequest));
    }


    @Test
    public void whenAllParamsOkAndServiceOkExpectAnPageItemResponseTest() throws IOException {
        getItemQueryRequest.setSite(SiteEnum.MLB.getSiteCode());
        getItemQueryRequest.setSortBy(SortsEnum.PRICE_ASC.getId());
        QueryDto queryDto = QueryDto.builder().build();
        when(validatorService.validarQueryParams(getItemQueryRequest)).thenReturn(queryDto);

        String pageItemMockJson = new String(Files.readAllBytes(Paths.get(PAGE_ITEM_RESPONSE_JSON_PATH)));
        Gson gson = new Gson();
        PageItemResponse pageItemResponse = gson.fromJson(pageItemMockJson, PageItemResponse.class);
        when(itemService.getItemsByQuery(any())).thenReturn(pageItemResponse);
        ResponseEntity<PageItemResponse> response = itemController.getItemBySite(getItemQueryRequest);
        assertEquals(pageItemResponse, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }






}
