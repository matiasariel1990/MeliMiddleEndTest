package com.meli.middleend.client.impl;

import com.meli.middleend.client.ApiMLClient;
import com.meli.middleend.client.ListenerUpState;
import com.meli.middleend.client.Notifier;
import com.meli.middleend.dto.api.client.SearByQueryDto;
import com.meli.middleend.dto.api.client.response.ItemByIdResponse;
import com.meli.middleend.dto.api.client.response.ItemDescription;
import com.meli.middleend.dto.api.client.response.SearchResponse;
import com.meli.middleend.exception.ApiUnavailableStateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Qualifier("ApiUnavailableState")
public class ApiUnavailableState implements ApiMLClient, Notifier {

    private static final Logger logger = LogManager.getLogger(ApiUnavailableState.class);

    @Value("${api.mercadolibre.rules.secondsRestartAwait}")
    int secondsAwait;

    private ListenerUpState listenerState;


    public void notifyAfterWait() {
        new Thread(() -> {
            try {
                Thread.sleep(secondsAwait * 1000L);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("El hilo fue interrumpido: {}", e.getMessage());
            }
            listenerState.setUpState();
        }).start();
    }


    @Override
    public SearchResponse searchByQuery(SearByQueryDto searByQueryDto) {
        throw new ApiUnavailableStateException();
    }

    @Override
    public ItemByIdResponse getItemById(String id) {
        throw new ApiUnavailableStateException();
    }

    @Override
    public ItemDescription getItemDescription(String id) {
        throw new ApiUnavailableStateException();
    }

    @Override
    public void setListener(ListenerUpState listenerState) {
        this.listenerState = listenerState;
        notifyAfterWait();

    }
}
