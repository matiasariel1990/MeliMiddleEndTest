package com.meli.middleend.client.impl;

import com.meli.middleend.client.support.CheckHealth;
import com.meli.middleend.dto.ApiCheckResult;
import com.meli.middleend.exception.ApiChangeStateException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
@Qualifier("ControlHealthImpl")
@Setter
public class ControlHealthImpl implements CheckHealth {


    LinkedList<ApiCheckResult> lastAttempts = new LinkedList<>();

    @Value("${api.mercadolibre.rules.maxSizeAttempts}")
    int maxSizeAttempts;

    @Value("${api.mercadolibre.rules.maxErrorPercentage}")
    int maxErrorPercentage;

    @Value("${api.mercadolibre.rules.maxConsecutiveFailures}")
    int maxConsecutiveFailures;

    private static final int MIN_SIZE_PORCENTAGEE = 10;


    @Override
    public void addOkRecord(ApiCheckResult apiCheckResult) {
        this.addRecord(apiCheckResult);
    }

    private void addRecord(ApiCheckResult apiCheckResult) {
        if(lastAttempts.size() >= maxSizeAttempts){
            lastAttempts.removeFirst();
        }
        lastAttempts.add(apiCheckResult);
    }

    @Override
    public void addErrorRecord(ApiCheckResult apiCheckResult) {
        this.addRecord(apiCheckResult);
        this.checkRecentRecords();
        this.checkPercentageLastRecords();
    }

    private void checkPercentageLastRecords() {

        if(lastAttempts.size() > MIN_SIZE_PORCENTAGEE && validPercentage(maxErrorPercentage) ){
            double prom = lastAttempts.stream()
                    .filter(result -> !result.isResultOk())
                    .count() / (double) lastAttempts.size();
            if(prom * 100 >= maxErrorPercentage){
                reset();
                throw new ApiChangeStateException();
            }

        }
    }

    private boolean validPercentage(int maxErrorPercentage) {
        /*
            Definicion: El max % de Error por config no puede superar
            el 25% sino no lo tengo en cuenta y lo considero como mal
            utilizado.
         */
        return MIN_SIZE_PORCENTAGEE > (maxErrorPercentage * 0.4);
    }

    private void checkRecentRecords() {
        if(lastAttempts.size() <= maxConsecutiveFailures) return;
        boolean isBadState = true;
        for (int i = lastAttempts.size() - maxConsecutiveFailures; i < lastAttempts.size(); i++) {
            if (lastAttempts.get(i).isResultOk()) {
                isBadState = false;
                break;
            }
        }
        if(isBadState){
            reset();
            throw new ApiChangeStateException();
        }
    }

    private void reset(){
        this.lastAttempts = new LinkedList<>();
    }
}
