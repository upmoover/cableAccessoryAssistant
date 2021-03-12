package org.upmoover.cableAccessoryAssistant.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CableServiceTest {
    @Autowired
    private CableService cableService;

    @Test
    void countAccessoriesNoStartAccessory() {
//инициализация объектов
        CableService mock = Mockito.mock(CableService.class);
//логика теста методов
//        assertEquals();
//
    }
}