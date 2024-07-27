package com.meli.middleend.service.impl;

import com.meli.middleend.dto.QueryDto;
import com.meli.middleend.dto.enums.UserEnum;
import com.meli.middleend.dto.response.ItemResponse;
import com.meli.middleend.dto.response.PageItemResponse;
import com.meli.middleend.exception.AuthException;
import com.meli.middleend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Qualifier("StrategyServiceImpl")
public class StrategyItemServiceImpl implements ItemService {


    ItemService strategy;

    @Autowired
    @Qualifier("MockItemServiceImpl")
    ItemService mockService;

    @Autowired
    @Qualifier("ItemServiceImpl")
    ItemService itemService;

    @Override
    public ItemResponse getItemById(String id) {
        setStrategy();
        return strategy.getItemById(id);
    }


    @Override
    public PageItemResponse getItemsByQuery(QueryDto query) {
        setStrategy();
        return strategy.getItemsByQuery(query);
    }

    public void setStrategy() {
        String roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if (roles.contains(UserEnum.USER_MOCK.getRole())){
            strategy = mockService;
            return;
        }
        if(roles.contains(UserEnum.USER_CLIENT.getRole())){
            strategy = itemService;
            return;
        }
        throw new AuthException();
    }
}
