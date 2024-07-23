package com.meli.middleend.factory;

import com.meli.middleend.dto.ItemDeteail;
import com.meli.middleend.dto.Price;
import com.meli.middleend.utils.MockConstants;

public class FactoryItem {

    public static ItemDeteail getMockItemDetail(String id){
        ItemDeteail itemDeteail = new ItemDeteail();
        itemDeteail.setId(id);
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
        return itemDeteail;
    }
}
