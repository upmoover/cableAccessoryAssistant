package org.upmoover.cableAccessoryAssistant.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.upmoover.cableAccessoryAssistant.entities.Cable;
import org.upmoover.cableAccessoryAssistant.services.CableService;

@Component
public class CheckUniqueness {

    static CableService cableService;

    @Autowired
    public CheckUniqueness(CableService cableService) {
        CheckUniqueness.cableService = cableService;
    }

    public static boolean isCableInTheBase(Cable cable) {
        //если кабель найден
        if ((cableService.isCableInTheDatabase(cable.getName()))) {
            return true;
        }
        //кабеля нет в базе
        else return false;

    }
}
