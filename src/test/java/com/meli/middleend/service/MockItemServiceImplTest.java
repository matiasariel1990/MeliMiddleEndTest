package com.meli.middleend.service;

import com.meli.middleend.dto.QueryDto;
import com.meli.middleend.dto.enums.SortsEnum;
import com.meli.middleend.dto.response.ItemResponse;
import com.meli.middleend.dto.response.PageItemResponse;
import com.meli.middleend.exception.ServiceException;
import com.meli.middleend.service.impl.MockItemServiceImpl;
import com.meli.middleend.utils.MockConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MockItemServiceImplTest {

    MockItemServiceImpl mockItemService;


    @Test
    public void getItemMockeadoTest(){

        mockItemService = new MockItemServiceImpl();

        ItemResponse itemResponse = mockItemService.getItemById(MockConstants.MOCK_ID_ITEM);

        //Item
        assertEquals(MockConstants.MOCK_ID_ITEM, itemResponse.getItem().getId());
        assertEquals(MockConstants.MOCK_TITLE,itemResponse.getItem().getTitle());
        assertEquals(MockConstants.MOCK_DESCRIPTION, itemResponse.getItem().getDescription());
        assertEquals(MockConstants.MOCK_PICTURE, itemResponse.getItem().getPicture());
        assertEquals(MockConstants.MOCK_SOLD_QUANTITY, itemResponse.getItem().getSold_quantity());
        //Price
        assertEquals(MockConstants.MOCK_PRICE, itemResponse.getItem().getPrice().getAmount());
        assertEquals(MockConstants.MOCK_DECIMAL, itemResponse.getItem().getPrice().getDecimals());
        assertEquals(MockConstants.MOCK_CURRENCY, itemResponse.getItem().getPrice().getCurrency());
        //Author
        assertEquals(MockConstants.MOCK_LAST_NAME_AUTHOR, itemResponse.getAuthor().getLastName());
        assertEquals(MockConstants.MOCK_NAME_AUTHOR, itemResponse.getAuthor().getName());
    }


    @Test void get5ItemsWhitOffsetTest(){
        mockItemService = new MockItemServiceImpl();

        QueryDto queryDto = QueryDto.builder()
                .query("query")
                .offset(3)
                .limit(5)
                .build();

        PageItemResponse page = mockItemService.getItemsByQuery(queryDto);

        assertEquals(5, page.getItems().size());
        assertEquals(5, page.getPaging().getLimit());
        assertEquals(3, page.getPaging().getOffset());
        assertEquals("Item4", page.getItems().get(0).getId());
        assertEquals("Item5", page.getItems().get(1).getId());
        assertEquals("Item6", page.getItems().get(2).getId());
        assertEquals("Item7", page.getItems().get(3).getId());
        assertEquals("Item8", page.getItems().get(4).getId());
    }

    @Test
    public void get5ItemWhitOffset3WhitOrderDescTest(){

        mockItemService = new MockItemServiceImpl();

        QueryDto queryDto = QueryDto.builder()
                .query("query")
                .sortEnum(SortsEnum.PRICE_DESC)
                .offset(2)
                .limit(5)
                .build();

        PageItemResponse page = mockItemService.getItemsByQuery(queryDto);

        assertEquals(5, page.getItems().size());
        assertEquals(5, page.getPaging().getLimit());
        assertEquals(2, page.getPaging().getOffset());
        assertTrue(page.getItems().get(0).getPrice().getAmount() >= page.getItems().get(1).getPrice().getAmount());
        assertTrue(page.getItems().get(1).getPrice().getAmount() >= page.getItems().get(2).getPrice().getAmount());
        assertTrue(page.getItems().get(2).getPrice().getAmount() >= page.getItems().get(3).getPrice().getAmount());
        assertTrue(page.getItems().get(3).getPrice().getAmount() >= page.getItems().get(4).getPrice().getAmount());
    }


    @Test
    public void get5ItemWhitOffset3WhitOrderAscTest(){

        mockItemService = new MockItemServiceImpl();

        QueryDto queryDto = QueryDto.builder()
                .query("query")
                .sortEnum(SortsEnum.PRICE_ASC)
                .offset(2)
                .limit(5)
                .build();

        PageItemResponse page = mockItemService.getItemsByQuery(queryDto);

        assertEquals(5, page.getItems().size());
        assertEquals(5, page.getPaging().getLimit());
        assertEquals(2, page.getPaging().getOffset());
        assertTrue(page.getItems().get(0).getPrice().getAmount() <= page.getItems().get(1).getPrice().getAmount());
        assertTrue(page.getItems().get(1).getPrice().getAmount() <= page.getItems().get(2).getPrice().getAmount());
        assertTrue(page.getItems().get(2).getPrice().getAmount() <= page.getItems().get(3).getPrice().getAmount());
        assertTrue(page.getItems().get(3).getPrice().getAmount() <= page.getItems().get(4).getPrice().getAmount());
    }

    @Test
    public void ifLimitIsBiggerThanMaxItemExcpectedServiceExceptionTest(){
        mockItemService = new MockItemServiceImpl();

        QueryDto queryDto = QueryDto.builder()
                .query("query")
                .sortEnum(SortsEnum.PRICE_ASC)
                .offset(2)
                .limit( MockConstants.MOCK_MAX_LIMIT +1 )
                .build();

        assertThrows(ServiceException.class, () -> mockItemService.getItemsByQuery(queryDto));
    }

    @Test
    public void ifLimitPLusOffSetIsBiggerThanMaxItemExcpectedServiceExceptionTest(){
        mockItemService = new MockItemServiceImpl();

        QueryDto queryDto = QueryDto.builder()
                .query("query")
                .sortEnum(SortsEnum.PRICE_ASC)
                .offset(2)
                .limit( MockConstants.MOCK_MAX_LIMIT - 1 )
                .build();

        assertThrows(ServiceException.class, () -> mockItemService.getItemsByQuery(queryDto));
    }





}
