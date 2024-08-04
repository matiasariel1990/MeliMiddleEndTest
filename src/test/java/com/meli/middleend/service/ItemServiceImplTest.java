package com.meli.middleend.service;

import com.meli.middleend.client.ApiMLClient;
import com.meli.middleend.client.ApiUnavailableStateTest;
import com.meli.middleend.dto.QueryDto;
import com.meli.middleend.dto.api.client.response.*;
import com.meli.middleend.dto.enums.SiteEnum;
import com.meli.middleend.dto.enums.SortsEnum;
import com.meli.middleend.dto.response.ItemResponse;
import com.meli.middleend.dto.response.PageItemResponse;
import com.meli.middleend.dto.response.Paging;
import com.meli.middleend.exception.ApiUnavailableStateException;
import com.meli.middleend.exception.ServiceClientException;
import com.meli.middleend.exception.ServiceException;
import com.meli.middleend.service.impl.ItemServiceImpl;
import com.meli.middleend.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.meli.middleend.utils.StringConstants.NO_AVAILABLE_DATA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemServiceImplTest {

    private static final String IDMOCK = "IdMock";
    private static final String TITLEMOCK = "titleMock";
    private static final String CATEGORYMOCK = "categoryMock";
    private static final String SELLERID = "SellerIdMock";
    private static final String DESCRIPTIONMOCK = "This is a mock description";
    private static final String ARSMOCK = "MockArs";
    private static final String CONDITIONMOCK = "ConditionMock";
    private static final String URLFALSE = "urlFalse";


    ItemServiceImpl itemServiceImpl;

    ApiMLClient apiMLClient;

    @BeforeEach
    public void setUp(){
        itemServiceImpl = new ItemServiceImpl();
        apiMLClient = mock(ApiMLClient.class);
        itemServiceImpl.setApiMLClient(apiMLClient);
    }

    @Test
    public void getItemByIdOkTest(){
        ItemByIdResponse itemByIdResponse = setItemByIdResponseMock();

        when(apiMLClient.getItemById(IDMOCK)).thenReturn(itemByIdResponse);
        ItemDescription itemDescription = new ItemDescription();
        itemDescription.setPlain_text(DESCRIPTIONMOCK);
        when(apiMLClient.getItemDescription(IDMOCK)).thenReturn(itemDescription);

        ItemResponse itemResponse = itemServiceImpl.getItemById(IDMOCK);

        assertEquals(IDMOCK, itemResponse.getItem().getId());
        assertEquals(TITLEMOCK, itemResponse.getItem().getTitle());
        assertEquals(CONDITIONMOCK, itemResponse.getItem().getCondition());
        assertEquals(SELLERID, itemResponse.getAuthor().getName());
        assertEquals(1200055L, itemResponse.getItem().getPrice().getAmount());
        assertEquals(ARSMOCK, itemResponse.getItem().getPrice().getCurrency());
        assertEquals(2, itemResponse.getItem().getPrice().getDecimals());
        assertEquals(DESCRIPTIONMOCK, itemResponse.getItem().getDescription());
    }

    @Test
    public void ifGetItemResponseOkButGetDescriptionNotOkExpectAnItemButNoDescriptionTest(){
        ItemByIdResponse itemByIdResponse = setItemByIdResponseMock();

        when(apiMLClient.getItemById(IDMOCK)).thenReturn(itemByIdResponse);

        when(apiMLClient.getItemDescription(IDMOCK)).thenThrow(ServiceClientException.class);

        ItemResponse itemResponse = itemServiceImpl.getItemById(IDMOCK);

        assertEquals(IDMOCK, itemResponse.getItem().getId());
        assertEquals(TITLEMOCK, itemResponse.getItem().getTitle());
        assertEquals(CONDITIONMOCK, itemResponse.getItem().getCondition());
        assertEquals(SELLERID, itemResponse.getAuthor().getName());
        assertEquals(1200055L, itemResponse.getItem().getPrice().getAmount());
        assertEquals(ARSMOCK, itemResponse.getItem().getPrice().getCurrency());
        assertEquals(2, itemResponse.getItem().getPrice().getDecimals());
        assertEquals(NO_AVAILABLE_DATA, itemResponse.getItem().getDescription());
    }

    private ItemByIdResponse setItemByIdResponseMock() {
        ItemByIdResponse itemByIdResponse = new ItemByIdResponse();
        itemByIdResponse.setId(IDMOCK);
        itemByIdResponse.setTitle(TITLEMOCK);
        Shipping shippingFalse = new Shipping();
        shippingFalse.setFree_shipping(false);
        itemByIdResponse.setShipping(shippingFalse);
        itemByIdResponse.setPrice(new BigDecimal("12000.55"));
        itemByIdResponse.setCategory_id(CATEGORYMOCK);
        itemByIdResponse.setCondition(CONDITIONMOCK);
        itemByIdResponse.setSeller_id(SELLERID);
        itemByIdResponse.setCurrency_id(ARSMOCK);
        return itemByIdResponse;
    }

    @Test
    public void whenApiResponseAnExceptionExpectedTheSameExceptionTest(){
        when(apiMLClient.getItemById(IDMOCK)).thenThrow(ServiceClientException.class);

        assertThrows(ServiceClientException.class, ()-> itemServiceImpl.getItemById(IDMOCK));

    }

    @Test
    public void whenApiResponseAnExceptionExpectedTheSameException2Test(){

        when(apiMLClient.getItemById(IDMOCK)).thenThrow(ApiUnavailableStateException.class);

        assertThrows(ApiUnavailableStateException.class, ()-> itemServiceImpl.getItemById(IDMOCK));
    }

    @Test
    public void searchByQueryTest(){
        QueryDto queryDto = QueryDto.builder()
                .siteEnum(SiteEnum.MLB)
                .query("query")
                .offset(1)
                .sortEnum(SortsEnum.PRICE_DESC)
                .limit(2).build();

        Shipping shipping = new Shipping();
        shipping.setFree_shipping(false);

        ItemResultSearch itemResultSearch =
                new ItemResultSearch();
        itemResultSearch.setId(IDMOCK);
        itemResultSearch.setPrice(new BigDecimal("123.55"));
        itemResultSearch.setThumbnail(URLFALSE);
        itemResultSearch.setTitle(TITLEMOCK);
        itemResultSearch.setCondition(CONDITIONMOCK);
        itemResultSearch.setCategory_id(CATEGORYMOCK);
        itemResultSearch.setShipping(shipping);

        ItemResultSearch itemResultSearch2 =
                new ItemResultSearch();
        itemResultSearch2.setId(IDMOCK + "2");
        itemResultSearch2.setPrice(new BigDecimal("123.55"));
        itemResultSearch2.setThumbnail(URLFALSE + "2");
        itemResultSearch2.setTitle(TITLEMOCK + "2");
        itemResultSearch2.setCondition(CONDITIONMOCK + "2");
        itemResultSearch2.setCategory_id(CATEGORYMOCK + "2");
        itemResultSearch2.setShipping(shipping);

        SearchResponse searchResponse = new SearchResponse();
        List<ItemResultSearch> itemResultList = new ArrayList<>();
        itemResultList.add(itemResultSearch);
        itemResultList.add(itemResultSearch2);

        Paging pagginMock = new Paging();
        pagginMock.setLimit(2);
        pagginMock.setTotal(300);
        pagginMock.setOffset(1);

        searchResponse.setPaging(pagginMock);
        searchResponse.setResults(itemResultList);

        when(apiMLClient.searchByQuery(any()))
                .thenReturn(searchResponse);

        PageItemResponse response = itemServiceImpl.getItemsByQuery(queryDto);

        assertEquals(300, response.getPaging().getTotal());
        assertTrue(response.getCategories().contains(CATEGORYMOCK));
        assertTrue(response.getCategories().contains(CATEGORYMOCK + "2"));
        assertEquals(IDMOCK, response.getItems().get(0).getId());
        assertEquals(IDMOCK + "2", response.getItems().get(1).getId());
        assertEquals(TITLEMOCK, response.getItems().get(0).getTitle());
        assertEquals(TITLEMOCK + "2", response.getItems().get(1).getTitle());
    }
}
