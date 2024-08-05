package com.meli.middleend.service.impl;

import com.meli.middleend.dto.QueryDto;
import com.meli.middleend.dto.enums.SiteEnum;
import com.meli.middleend.dto.enums.SortsEnum;
import com.meli.middleend.dto.request.GetItemQueryRequest;
import com.meli.middleend.exception.ValidationException;
import com.meli.middleend.service.ValidatorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class ValidatorServiceImpl implements ValidatorService {

    private static final String INVALID_SITE = "Invalid Site";
    private static final String LIMIT_MAX = "The maximum allowed limit is :";
    private static final String LIMIT_MIN = "The maximum allowed limit is : ";
    private static final String OFF_SET_NEGATIVE = "Offset value can't be negative";
    private static final String REGEX_VALID = "^[a-zA-Z0-9_\\s]+$";
    private static final String INVALID_ID_WHIT_SPACES= "Id can't have spaces";
    private static final String INVALID_ID_NULL = "Id can't be null";
    private static final String ID_MAX_SIZE = "The maximum size of id is: ";
    private static final String QUERY_MAX_SIZE_ERROR = "Query size too long." ;
    private static final String QUERY_REGEX_ERROR = "Invalid param";
    private static final String QUERY_NULL = "Query value can't be null";
    private static final int DEFAULT_OFFSET = 0;

    @Value("${request.search.param.max.limit}")
    private int maxLimit;

    @Value("${request.search.param.min.limit}")
    private int minLimit;

    @Value("${request.search.param.max.query.size}")
    private int maxQuerySize;

    @Value("${request.id.var.path.max.id}")
    private int maxIdSize;


    @Override
    public QueryDto validarQueryParams(GetItemQueryRequest getItemQueryRequest) {
        QueryDto queryDto = QueryDto.builder().build();
        queryDto.setSiteEnum(validarSite(getItemQueryRequest.getSite()));
        queryDto.setQuery(validarQuery(getItemQueryRequest.getQuery()));
        if(getItemQueryRequest.getOffset() != DEFAULT_OFFSET){
            queryDto.setOffset(validarOffset(getItemQueryRequest.getOffset()));
        }
        queryDto.setLimit(validarLimit(getItemQueryRequest.getLimit()));
        if(getItemQueryRequest.getSortBy() != null){
            queryDto.setSortEnum(validarSort(getItemQueryRequest.getSortBy()));
        }
        return queryDto;
    }

    @Override
    public SiteEnum validarSite(String site) {
        validarRegex(site);
        try {
            return SiteEnum.valueOf(site);
        }catch (Exception e){
            throw new ValidationException(INVALID_SITE);
        }
    }

    @Override
    public String validarQuery(String query) {
        if(query == null){
            throw new ValidationException(QUERY_NULL);
        }
        if(query.length() > maxQuerySize){
            throw new ValidationException(QUERY_MAX_SIZE_ERROR);
        }
        validarRegex(query);

        return query;
    }

    private void validarRegex(String var) {
        if(var == null || !var.matches(REGEX_VALID)){
            throw new ValidationException(QUERY_REGEX_ERROR);
        }
    }

    @Override
    public SortsEnum validarSort(String sortBy) {
        validarRegex(sortBy);
        try {
            return SortsEnum.valueOf(sortBy.toUpperCase());
        }catch (Exception e){
            throw new ValidationException(INVALID_SITE);
        }
    }

    @Override
    public int validarOffset(int offset) {
        if(offset < 0){
            throw new ValidationException(OFF_SET_NEGATIVE);
        }
        return offset;
    }

    @Override
    public int validarLimit(int limit) {
        if(limit < minLimit){
            throw new ValidationException(LIMIT_MIN + minLimit);
        }
        if(limit > maxLimit){
            throw new ValidationException(LIMIT_MAX + maxLimit);
        }
        return limit;
    }


    @Override
    public String validarId(String id) {
        if(id == null){
            throw new ValidationException(INVALID_ID_NULL);
        }
        validarRegex(id);
        if(id.contains(" ")){
            throw new ValidationException(INVALID_ID_WHIT_SPACES);
        }
        if(id.length() > maxIdSize){
            throw new ValidationException(ID_MAX_SIZE + maxIdSize);
        }
        return id;
    }



}
