package com.meli.middleend.client;

import com.meli.middleend.client.impl.ControlHealthImpl;
import com.meli.middleend.dto.ApiCheckResult;
import com.meli.middleend.exception.ApiChangeStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ControlHealthTest {

    ControlHealthImpl controlHealth;
    int maxSizeAttempts = 100;

    int maxErrorPercentage = 20;

    int maxConsecutiveFailures = 5;

    @BeforeEach
    public void setUp(){
        controlHealth = new ControlHealthImpl();
        controlHealth.setMaxSizeAttempts(maxSizeAttempts);
        controlHealth.setMaxErrorPercentage(maxErrorPercentage);
        controlHealth.setMaxConsecutiveFailures(maxConsecutiveFailures);
    }

    @Test
    public void couldReceiveALotOfRequestOkAndNotTrhowsExceptionTest(){
        for(int i = 0; i < 200; i++){
            controlHealth.addOkRecord(ApiCheckResult.ApiCheckResultBuild());
        }
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        assertDoesNotThrow(()-> controlHealth.addOkRecord(ApiCheckResult.ApiCheckResultBuild()));
    }


    @Test
    public void afterALotOfResultOkGet5BadAndExpectedAnExceptionInLastTest(){
        for(int i = 0; i < 200; i++){
            controlHealth.addOkRecord(ApiCheckResult.ApiCheckResultBuild());
        }
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        assertThrows(ApiChangeStateException.class, ()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
    }


    @Test
    public void afterALotOfResultOkGet2badAnd1OKAndAfter5BadAndExpectedAnExceptionInLastTest(){
        for(int i = 0; i < 200; i++){
            controlHealth.addOkRecord(ApiCheckResult.ApiCheckResultBuild());
        }
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        controlHealth.addOkRecord(ApiCheckResult.ApiCheckResultBuild());
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        assertThrows(ApiChangeStateException.class, ()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        //Affter ApiChangeStateException reset count
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
    }


    @Test
    public void afterALotOfResultOkReceive2BadAnd1OktoHaveAnExceptionForMaxPercentTest(){
        for(int i = 0; i < 200; i++){
            controlHealth.addOkRecord(ApiCheckResult.ApiCheckResultBuild());
        }
        //if max is 100 20% are 20 bad calls
        for(int i = 0; i < 9; i++){
            assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
            assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
            controlHealth.addOkRecord(ApiCheckResult.ApiCheckResultBuild());
        }
        //if max is 100 20% are 20 bad calls
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        assertThrows(ApiChangeStateException.class, ()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        //Affter ApiChangeStateException reset count
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
        assertDoesNotThrow(()-> controlHealth.addErrorRecord(ApiCheckResult.ApiCheckBadResultBuild("404")));
    }
}
