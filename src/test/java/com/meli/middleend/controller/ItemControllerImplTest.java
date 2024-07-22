package com.meli.middleend.controller;


import com.meli.middleend.controller.impl.ItemControllerImpl;
import com.meli.middleend.dto.ItemDeteail;
import com.meli.middleend.dto.response.ErrorResponse;
import com.meli.middleend.dto.response.ItemResponse;
import com.meli.middleend.exception.ServiceException;
import com.meli.middleend.factory.FactoryItem;
import com.meli.middleend.service.ItemService;
import com.meli.middleend.utils.MockConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemControllerImplTest {

    private static final String ID_MOCK = "unId";

    private static final String ERROR_MESSAGE_MOCK = "A message error.";

    @InjectMocks
    ItemControllerImpl itemController;

    @Mock
    ItemService itemService;

    @Test
    public void whenItemServiceResponsesOkExpected200AndEntityItemResponseTest(){

        ItemDeteail itemDeteail = FactoryItem.getMockItemDetail(ID_MOCK);

        when(itemService.getItemById(ID_MOCK)).thenReturn(
                ItemResponse.builder()
                        .item(itemDeteail)
                    .build());

        ResponseEntity<ItemResponse> entityResponse = (ResponseEntity<ItemResponse>)itemController.getItem(ID_MOCK);

        assertEquals(HttpStatus.OK, entityResponse.getStatusCode());

        assertEquals(ID_MOCK, entityResponse.getBody().getItem().getId());
        assertEquals(MockConstants.MOCK_DESCRIPTION, entityResponse.getBody().getItem().getDescription());
    }

    @Test void whenItemServicThrowsAnExceptionExpectedAnExceptionTest(){

        when(itemService.getItemById(ID_MOCK)).thenThrow(new ServiceException(ERROR_MESSAGE_MOCK));

        assertThrows(ServiceException.class, () ->itemController.getItem(ID_MOCK));

    }
}
