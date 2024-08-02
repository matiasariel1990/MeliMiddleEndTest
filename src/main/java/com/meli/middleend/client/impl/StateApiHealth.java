package com.meli.middleend.client.impl;

import com.meli.middleend.client.ApiMLClient;
import com.meli.middleend.client.ListenerUpState;
import com.meli.middleend.client.Notifier;
import com.meli.middleend.dto.api.client.SearByQueryDto;
import com.meli.middleend.dto.api.client.response.ItemByIdResponse;
import com.meli.middleend.dto.api.client.response.ItemDescription;
import com.meli.middleend.dto.api.client.response.SearchResponse;
import com.meli.middleend.exception.ApiChangeStateException;
import com.meli.middleend.exception.ServiceClientException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("StateApiHealth")
public class StateApiHealth implements ApiMLClient, ListenerUpState {

    private static final String SERVICE_DOWN = "The service is not responding correctly; it will be temporarily disabled.";

    ApiMLClient state;

    @Autowired
    @Qualifier("ApiUnavailableState")
    ApiMLClient apiUnavailableState;

    @Autowired
    @Qualifier("ApiClientImpl")
    ApiMLClient apiMLClient;

    int timeResetApiClient;

    @PostConstruct
    public void init() {
        this.state = apiMLClient;
    }


    @Override
    public SearchResponse searchByQuery(SearByQueryDto searByQueryDto) {
        try {
            return state.searchByQuery(searByQueryDto);
        }catch (ApiChangeStateException apiChangeStateException){
            this.setToFaultyState();
            throw new ServiceClientException(SERVICE_DOWN);
        }
    }

    @Override
    public ItemByIdResponse getItemById(String id) {
        try {
            return state.getItemById(id);
        }catch (ApiChangeStateException apiChangeStateException){
            this.setToFaultyState();
            throw new ServiceClientException(SERVICE_DOWN);
        }
    }

    @Override
    public ItemDescription getItemDescription(String id) {
        try {
            return state.getItemDescription(id);
        }catch (ApiChangeStateException apiChangeStateException){
            this.setToFaultyState();
            throw new ServiceClientException(SERVICE_DOWN);
        }
    }

    public void setToFaultyState(){
        state = apiUnavailableState;
        Notifier notifier = (Notifier)  apiUnavailableState;
        notifier.setListener(this);
    }

    public void setUpState(){
        this.state = apiMLClient;
    }
}
