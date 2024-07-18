package com.meli.middleend.service.impl;

import com.meli.middleend.dto.*;
import com.meli.middleend.dto.response.ItemResponse;
import com.meli.middleend.dto.response.PageItemResponse;
import com.meli.middleend.dto.response.Paging;
import com.meli.middleend.exception.ServiceException;
import com.meli.middleend.service.ItemService;
import com.meli.middleend.utils.MockConstants;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.meli.middleend.utils.MockConstants.*;

@Service
public class MockItemServiceImpl implements ItemService {

    @Override
    public ItemResponse getItemById(String id) {

        Author author = Author.builder()
                .name(MockConstants.MOCK_NAME_AUTHOR)
                .lastName(MockConstants.MOCK_LAST_NAME_AUTHOR)
                .build();

        ItemDeteail itemDeteail = new ItemDeteail();
        itemDeteail.setId(MockConstants.MOCK_ID_ITEM);
        Price price = Price.builder()
                .amount(MockConstants.MOCK_PRICE)
                .currency(MockConstants.MOCK_CURRENCY)
                .decimals(MockConstants.MOCK_DECIMAL).build();
        itemDeteail.setPrice(price);
        itemDeteail.setPicture(MockConstants.MOCK_PICTURE);
        itemDeteail.setDescription(MockConstants.MOCK_DESCRIPTION);
        itemDeteail.setFree_shipping(MockConstants.MOCK_FREE_SHIPPING);
        itemDeteail.setCondition(MockConstants.MOCK_CONDITION);
        itemDeteail.setTitle(MockConstants.MOCK_TITLE);
        itemDeteail.setSold_quantity(MockConstants.MOCK_SOLD_QUANTITY);

        return ItemResponse.builder()
                .author(author).item(itemDeteail).build();

    }

    @Override
    public PageItemResponse getItemsByQuery(QueryDto query) {

        List<Item> items = new ArrayList<>();
        Set<String> categories = new HashSet<>();
        Paging paging = new Paging();
        if(MOCK_MAX_LIMIT < query.getLimit() + query.getOffset()){
            throw new ServiceException();
        }
        paging.setTotal(MOCK_MAX_LIMIT);
        paging.setOffset(query.getOffset());
        paging.setLimit(query.getLimit());
        int offSet = query.getOffset();
        while (items.size() < query.getLimit()){
            Item item = createMockItem(offSet + 1);
            offSet++;
            items.add(item);
            if(items.size() < 3){
                categories.add("Category_" + items.size());
            }
        }

        if(query.getSortEnum() != null){
            if(SortsEnum.PRICE_ASC.equals(query.getSortEnum())){
                items= items.stream().sorted(
                        Comparator.comparingInt(a -> a.getPrice().getAmount())).collect(Collectors.toList());
            }else{
                items= items.stream().sorted(
                        Comparator.comparingInt(a -> a.getPrice().getAmount())).collect(Collectors.toList());
                Collections.reverse(items);
            }

        }


        return PageItemResponse.builder()
                .items(items)
                .categories(categories)
                .paging(paging)
                .build();
    }

    private Item createMockItem(int number) {
        Item item = new Item();
        item.setId("Item"+ number);
        item.setTitle(MockConstants.MOCK_TITLE + number);
        item.setCondition(MOCK_CONDITION);
        item.setFree_shipping(MOCK_FREE_SHIPPING);
        item.setPrice(createRandomPrice());
        return item;
    }

    private Price createRandomPrice() {
        return Price.builder()
                .amount(getRandomNumber())
                .currency(MockConstants.MOCK_CURRENCY)
                .decimals(MockConstants.MOCK_DECIMAL).build();
    }

    private int getRandomNumber() {
        Random random = new Random();
        int min = (int) Math.pow(10, 4);
        int max = (int) Math.pow(10, 8);
        return random.nextInt((max - min) + 1) + min;
    }

}
