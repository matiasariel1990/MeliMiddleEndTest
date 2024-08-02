package com.meli.middleend.client;

import com.meli.middleend.client.impl.ApiUnavailableState;
import com.meli.middleend.dto.ApiCheckResult;
import com.meli.middleend.dto.api.client.SearByQueryDto;
import com.meli.middleend.exception.ApiChangeStateException;
import com.meli.middleend.exception.ApiUnavailableStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ApiUnavailableStateTest {

    private class ListenerMock implements ListenerUpState{

        private boolean stateOk = false;


        public boolean getState(){
            return stateOk;
        }


        @Override
        public void setUpState() {
            stateOk = true;
        }
    }

    @Autowired
    ApiUnavailableState apiUnavailableState;


    @Test
    public void testMethodsAllShouldReturnAnExceptionTest(){

        apiUnavailableState.setListener(new ListenerMock());
        assertThrows(ApiUnavailableStateException.class, ()-> apiUnavailableState.getItemById("id"));
        assertThrows(ApiUnavailableStateException.class, ()-> apiUnavailableState.getItemDescription("id"));
        assertThrows(ApiUnavailableStateException.class, ()-> apiUnavailableState.searchByQuery(new SearByQueryDto()));

    }


    @Test
    public void notifyListenerTest() throws InterruptedException {
        ListenerMock listenerUpState = new ListenerMock();
        assertFalse(listenerUpState.getState());

        apiUnavailableState.setListener(listenerUpState);
        apiUnavailableState.notifyAfterWait();
        assertFalse(listenerUpState.getState());
        Thread.sleep(6000);
        assertTrue(listenerUpState.getState());

    }


}
