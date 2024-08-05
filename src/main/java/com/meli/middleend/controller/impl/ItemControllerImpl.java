package com.meli.middleend.controller.impl;

import com.meli.middleend.controller.ItemController;
import com.meli.middleend.dto.QueryDto;
import com.meli.middleend.dto.request.GetItemQueryRequest;
import com.meli.middleend.dto.response.ItemResponse;
import com.meli.middleend.dto.response.PageItemResponse;
import com.meli.middleend.exception.AuthException;
import com.meli.middleend.service.ItemService;
import com.meli.middleend.service.ValidatorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Setter
public class ItemControllerImpl implements ItemController {

    private static final int DEFAULT_OFFSET = 0;

    @Autowired
    @Qualifier("StrategyServiceImpl")
    ItemService itemService;

    @Autowired
    ValidatorService validatorService;

    @Override
    public ResponseEntity<PageItemResponse> getItemBySite(GetItemQueryRequest getItemQueryRequest) {
        QueryDto queryDto = validatorService.validarQueryParams(getItemQueryRequest);
        return new ResponseEntity<>(itemService.getItemsByQuery(queryDto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ItemResponse> getItem(String id) {

        return new ResponseEntity<>(itemService.getItemById(
                validatorService.validarId(id)
        ), HttpStatus.OK);
    }
}
