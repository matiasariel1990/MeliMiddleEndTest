package com.meli.middleend.utils;

import com.google.gson.Gson;
import com.meli.middleend.dto.*;
import com.meli.middleend.dto.api.client.SearByQueryDto;
import com.meli.middleend.dto.api.client.response.ItemByIdResponse;
import com.meli.middleend.dto.api.client.response.ItemResultSearch;
import com.meli.middleend.dto.api.client.response.Picture;
import com.meli.middleend.dto.enums.SiteEnum;
import com.meli.middleend.dto.enums.SortsEnum;
import com.meli.middleend.dto.response.ItemResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.meli.middleend.utils.StringConstants.PICTURE_NO_AVAILABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MapperTest {

    private static final String QUERY_TEST = "Query test";
    private static final String ITEM_SEARCH_JSON_PATH = "src/test/resources/item_search_mock.json";
    private static final String ID_MOCK = "idMock";
    private static final String CONDITION_MOCK = "new";
    private static final BigDecimal AMOUNT_MOCK = new BigDecimal("1234.33");
    private static final int INICIAL_Q_MOCK = 1234;
    private static final String CATEGORY_MOCK = "categoryMock";
    private static final String CURRENCY_MOCK = "Ars";
    private static final String TITLE_MOCK = "Title Mock";

    @Test
    public void integrityQueryTest(){
        QueryDto mockQueryDto = QueryDto.builder()
                .query(QUERY_TEST).limit(10).offset(1).siteEnum(SiteEnum.MLB).build();
        SearByQueryDto searByQueryDto = Mapper.mapToSearByQueryDto(mockQueryDto);

        assertEquals(QUERY_TEST, searByQueryDto.getQuery());
        assertEquals(String.valueOf(10), searByQueryDto.getLimit());
        assertEquals(String.valueOf(1), searByQueryDto.getOffset());
        assertEquals(SiteEnum.MLB.getSiteCode(), searByQueryDto.getSite());
    }

    @Test
    public void integrityWhitoutOffsetQueryTest(){
        QueryDto mockQueryDto = QueryDto.builder()
                .query(QUERY_TEST).limit(10).offset(0).siteEnum(SiteEnum.MLA).build();
        SearByQueryDto searByQueryDto = Mapper.mapToSearByQueryDto(mockQueryDto);

        assertEquals(QUERY_TEST, searByQueryDto.getQuery());
        assertEquals(String.valueOf(10), searByQueryDto.getLimit());
        assertNull(searByQueryDto.getOffset());
        assertEquals(SiteEnum.MLA.getSiteCode(), searByQueryDto.getSite());
        assertNull(searByQueryDto.getSort());
    }

    @Test
    public void integrityWhitSortsetQueryTest(){
        QueryDto mockQueryDto = QueryDto.builder()
                .query(QUERY_TEST).limit(10).offset(0)
                .sortEnum(SortsEnum.PRICE_DESC).siteEnum(SiteEnum.MLA).build();
        SearByQueryDto searByQueryDto = Mapper.mapToSearByQueryDto(mockQueryDto);

        assertEquals(QUERY_TEST, searByQueryDto.getQuery());
        assertEquals(String.valueOf(10), searByQueryDto.getLimit());
        assertNull(searByQueryDto.getOffset());
        assertEquals(SiteEnum.MLA.getSiteCode(), searByQueryDto.getSite());
        assertEquals(SortsEnum.PRICE_DESC, searByQueryDto.getSort());
    }


    @Test
    public void mapToPriceTest(){
        BigDecimal monto = new BigDecimal("122222.34");
        Price price = Mapper.mapToPrice(monto, "ARS");
        assertEquals(12222234, price.getAmount());
        assertEquals("ARS", price.getCurrency());
        assertEquals(2, price.getDecimals());
    }

    @Test
    public void mapToPrice2Test(){
        BigDecimal monto = new BigDecimal("12854");
        Price price = Mapper.mapToPrice(monto, "BRS");
        assertEquals(12854, price.getAmount());
        assertEquals("BRS", price.getCurrency());
        assertEquals(0, price.getDecimals());
    }

    @Test
    public void mapToPrice3Test(){
        BigDecimal monto = new BigDecimal("12882.334");
        Price price = Mapper.mapToPrice(monto, "MX");
        assertEquals(12882334, price.getAmount());
        assertEquals("MX", price.getCurrency());
        assertEquals(3, price.getDecimals());
    }

    @Test
    public void mapToPrice4Test(){
        BigDecimal monto = new BigDecimal("128555582.3");
        Price price = Mapper.mapToPrice(monto, "MX");
        assertEquals(1285555823, price.getAmount());
        assertEquals("MX", price.getCurrency());
        assertEquals(1, price.getDecimals());
    }

    @Test
    public void mapToPriceBigNumberTest(){
        BigDecimal monto = new BigDecimal("92233720368547.7500");
        Price price = Mapper.mapToPrice(monto, "MX");
        assertEquals(922337203685477500L, price.getAmount());
        assertEquals("MX", price.getCurrency());
        assertEquals(4, price.getDecimals());
    }

    @Test
    public void mapIntegrityItemSearchTest() throws IOException {
        Gson gson = new Gson();
        String itemJsonMock = new String(Files.readAllBytes(Paths.get(ITEM_SEARCH_JSON_PATH)));
        ItemResultSearch itemSearch = gson.fromJson(itemJsonMock, ItemResultSearch.class);
        Item result = Mapper.mapToItem(itemSearch);

        assertEquals(itemSearch.getId(), result.getId());
        assertEquals(itemSearch.getCondition(), result.getCondition());
        assertEquals(itemSearch.getShipping().isFree_shipping(), result.isFree_shipping());
        assertEquals(itemSearch.getTitle(), result.getTitle());

        assertEquals(itemSearch.getCurrency_id(), result.getPrice().getCurrency());
        assertEquals(itemSearch.getThumbnail(), result.getPicture());
        assertEquals(itemSearch.getPrice().scale(), result.getPrice().getDecimals());
        long decimalPriceLongValue = itemSearch.getPrice().movePointRight(itemSearch.getPrice().scale()).longValueExact();
        assertEquals(decimalPriceLongValue, result.getPrice().getAmount());
    }

    @Test
    public void mapToItemDetailTest(){
        ItemByIdResponse itemByIdResponse = new ItemByIdResponse();
        itemByIdResponse.setId(ID_MOCK);
        itemByIdResponse.setTitle(TITLE_MOCK);
        itemByIdResponse.setCondition(CONDITION_MOCK);
        itemByIdResponse.setPrice(AMOUNT_MOCK);
        itemByIdResponse.setInitial_quantity(INICIAL_Q_MOCK);
        List<Picture> pictures = new ArrayList<>();
        pictures.add(new Picture("pic1"));
        pictures.add(new Picture("pic2"));
        itemByIdResponse.setPictures(pictures);
        itemByIdResponse.setCategory_id(CATEGORY_MOCK);
        itemByIdResponse.setCurrency_id(CURRENCY_MOCK);

        ItemDeteail itemDeteailResult =   Mapper.mapToItemDetail(itemByIdResponse);
        assertEquals(ID_MOCK, itemDeteailResult.getId());
        assertEquals(CONDITION_MOCK, itemDeteailResult.getCondition());
        assertEquals(AMOUNT_MOCK.movePointRight(AMOUNT_MOCK.scale()).longValueExact(), itemDeteailResult.getPrice().getAmount());
        assertEquals(CURRENCY_MOCK, itemDeteailResult.getPrice().getCurrency());
        assertEquals("pic1", itemDeteailResult.getPicture());
    }

    @Test
    public void mapToItemDetailWithoutPictureTest(){
        ItemByIdResponse itemByIdResponse = new ItemByIdResponse();
        itemByIdResponse.setId(ID_MOCK);
        itemByIdResponse.setTitle(TITLE_MOCK);
        itemByIdResponse.setCondition(CONDITION_MOCK);
        itemByIdResponse.setPrice(AMOUNT_MOCK);
        itemByIdResponse.setInitial_quantity(INICIAL_Q_MOCK);
        List<Picture> pictures = new ArrayList<>();
        itemByIdResponse.setPictures(pictures);
        itemByIdResponse.setCategory_id(CATEGORY_MOCK);
        itemByIdResponse.setCurrency_id(CURRENCY_MOCK);

        ItemDeteail itemDeteailResult =   Mapper.mapToItemDetail(itemByIdResponse);
        assertEquals(ID_MOCK, itemDeteailResult.getId());
        assertEquals(CONDITION_MOCK, itemDeteailResult.getCondition());
        assertEquals(AMOUNT_MOCK.movePointRight(AMOUNT_MOCK.scale()).longValueExact(), itemDeteailResult.getPrice().getAmount());
        assertEquals(CURRENCY_MOCK, itemDeteailResult.getPrice().getCurrency());
        assertEquals(PICTURE_NO_AVAILABLE, itemDeteailResult.getPicture());
    }


}
