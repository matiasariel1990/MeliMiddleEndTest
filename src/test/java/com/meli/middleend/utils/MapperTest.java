package com.meli.middleend.utils;

import com.google.gson.Gson;
import com.meli.middleend.dto.*;
import com.meli.middleend.dto.api.client.SearByQueryDto;
import com.meli.middleend.dto.api.client.response.ItemResultSearch;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MapperTest {

    private static final String QUERY_TEST = "Query test";
    private static final String ITEM_SEARCH_JSON_PATH = "src/test/resources/item_search_mock.json";

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
}
