package com.meli.middleend.service;

import com.meli.middleend.dto.QueryDto;
import com.meli.middleend.dto.enums.SiteEnum;
import com.meli.middleend.dto.enums.SortsEnum;
import com.meli.middleend.dto.request.GetItemQueryRequest;
import com.meli.middleend.exception.ValidationException;
import com.meli.middleend.service.impl.ValidatorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ValidatorServiceImplTest {

    @Configuration
    static class TestConfig {
        @Bean
        public ValidatorService validatorService() {
            return new ValidatorServiceImpl();
        }
    }

    @Autowired
    ValidatorService validatorService;

    @Test
    public void validarSiteTest(){

        assertEquals(SiteEnum.MLA, validatorService.validarSite(SiteEnum.MLA.getSiteCode()));
        assertEquals(SiteEnum.MLB, validatorService.validarSite("MLB"));
        assertThrows(ValidationException.class,() -> validatorService.validarSite("otroSite"));
        assertThrows(ValidationException.class,() -> validatorService.validarSite("aa"));
        assertThrows(ValidationException.class,() -> validatorService.validarSite(null));
    }

    @Test
    public void ifLimitIsLowerOrEqualsThanLimitMaxReturnThisLimitTest(){
        //Max limit 10 en application.properties
        assertEquals(5, validatorService.validarLimit(5));
        assertEquals(2, validatorService.validarLimit(2));
        assertEquals(10, validatorService.validarLimit(10));
        assertEquals(9, validatorService.validarLimit(9));
    }

    @Test
    public void ifLimitIsHigherThanLimitMaxExpectAnExceptionTest(){
        //Max limit 10 en application.properties
        assertThrows(ValidationException.class,() -> validatorService.validarLimit(25));
        assertThrows(ValidationException.class,() -> validatorService.validarLimit(55));
        assertThrows(ValidationException.class,() -> validatorService.validarLimit(125));
    }

    @Test
    public void ifSearchQueryIsTooLognExpectAnExceptionTest(){
        //Max size 25 en application.properties
       String queryMockSize ="123456789012345678901234567";
       assertThrows(ValidationException.class,() -> validatorService.validarQuery(queryMockSize));
    }

    @Test
    public void ifSearchQueryHaveSpecialCharacterExpectedAnExceptionTest(){
        //Max size 25 en application.properties
        String queryMockMatcher ="select * from";
        assertThrows(ValidationException.class,() -> validatorService.validarQuery(queryMockMatcher));
        String queryMockMatcher2 = "1=!";
        assertThrows(ValidationException.class,() -> validatorService.validarQuery(queryMockMatcher2));
        String queryMockMatcher3 = "1=1";
        assertThrows(ValidationException.class,() -> validatorService.validarQuery(queryMockMatcher3));
        String queryMockMatcher4 = "<script>alert('XSS')";
        assertThrows(ValidationException.class,() -> validatorService.validarQuery(queryMockMatcher4));
    }

    @Test
    public void ifIsAValidQueryItemReturnSameItemTest(){
        String itemQuery = "tele";
        assertEquals(itemQuery, validatorService.validarQuery(itemQuery));
        String itemQueryWhitSpaces = "tele con soporte";
        assertEquals(itemQueryWhitSpaces, validatorService.validarQuery(itemQueryWhitSpaces));
    }

    @Test
    public void ifTheQueryIsNullExpectAnExceptionTest(){
        assertThrows(ValidationException.class,() -> validatorService.validarQuery(null));
    }

    @Test
    public void offSetValidationTest(){
        assertThrows(ValidationException.class,() -> validatorService.validarOffset(-1));
        assertThrows(ValidationException.class,() -> validatorService.validarOffset(-10));
        assertThrows(ValidationException.class,() -> validatorService.validarOffset(-10000));
        assertEquals(3,validatorService.validarOffset(3)) ;
        assertEquals(4,validatorService.validarOffset(4)) ;
        assertEquals(5,validatorService.validarOffset(5)) ;
        assertEquals(6,validatorService.validarOffset(6)) ;
    }

    @Test
    public void ifSortByIsInvalidThrowsAServiceExceptionTest(){
        assertThrows(ValidationException.class,() -> validatorService.validarSort("someInvalidSort"));
        assertThrows(ValidationException.class,() -> validatorService.validarSort("otherIvalid"));
        assertThrows(ValidationException.class,() -> validatorService.validarSort("other I valid"));
    }

    @Test
    public void ifSortByIsInvalidReturnAnORderSortTest(){

        assertEquals(SortsEnum.PRICE_ASC, validatorService.validarSort("price_asc"));
        assertEquals(SortsEnum.PRICE_DESC, validatorService.validarSort("price_desc"));
        assertEquals(SortsEnum.PRICE_ASC, validatorService.validarSort("prIcE_aSc"));
    }

    @Test void ifIdIsNullExpectAnExceptionTest(){
        assertThrows(ValidationException.class,() -> validatorService.validarId(null));

    }

    @Test void ifIdIsBiggerThanLimitExpectAnExceptionTest(){
        //max size 12
        String idBigger = "213412431243123412341243123412341234123412341234";
        assertThrows(ValidationException.class,() -> validatorService.validarId(idBigger));

    }

    @Test void ifIdHaveAnInvalidCharacterExpectAnExceptionTest(){
        assertThrows(ValidationException.class,() -> validatorService.validarId("asdfa%"));
        assertThrows(ValidationException.class,() -> validatorService.validarId("as!"));
        assertThrows(ValidationException.class,() -> validatorService.validarId("df*"));
    }

    @Test void ifIdHaveASpaceExpectAnExceptionTest(){
        assertThrows(ValidationException.class,() -> validatorService.validarId("un id "));
        assertThrows(ValidationException.class,() -> validatorService.validarId("asdfsadfds "));
        assertThrows(ValidationException.class,() -> validatorService.validarId(" dfadf"));
    }

    @Test
    public void whenQueryIsInvalidExpectedAnExcepitionTest(){
        GetItemQueryRequest getItemQueryRequest = createMockGetItemQuery();
        getItemQueryRequest.setQuery("Select * from ");
        assertThrows(ValidationException.class,() -> validatorService.validarQueryParams(getItemQueryRequest));
    }

    @Test
    public void whenSITEIsInvalidExpectedAnExcepitionTest(){
        GetItemQueryRequest getItemQueryRequest = createMockGetItemQuery();
        getItemQueryRequest.setSite("MLK");
        assertThrows(ValidationException.class,() -> validatorService.validarQueryParams(getItemQueryRequest));
    }

    @Test
    public void whenOffsetIsInvalidExpectedAnExcepitionTest(){
        GetItemQueryRequest getItemQueryRequest = createMockGetItemQuery();
        getItemQueryRequest.setOffset(-1);
        assertThrows(ValidationException.class,() -> validatorService.validarQueryParams(getItemQueryRequest));
    }

    @Test
    public void iffAllIsOKTest(){
        GetItemQueryRequest getItemQueryRequest = createMockGetItemQuery();
        QueryDto queryDto = validatorService.validarQueryParams(getItemQueryRequest);

        assertEquals(getItemQueryRequest.getSite(), queryDto.getSiteEnum().getSiteCode());
        assertEquals(getItemQueryRequest.getOffset(), queryDto.getOffset());
        assertEquals(getItemQueryRequest.getSortBy(), queryDto.getSortEnum().getId());
        assertEquals(getItemQueryRequest.getQuery(), queryDto.getQuery());
    }

    private GetItemQueryRequest createMockGetItemQuery(){
        GetItemQueryRequest getItemQueryRequest = new GetItemQueryRequest();
        getItemQueryRequest.setSite("MLA");
        getItemQueryRequest.setQuery("query");
        getItemQueryRequest.setSortBy(SortsEnum.PRICE_ASC.getId());
        return getItemQueryRequest;
    }
}
