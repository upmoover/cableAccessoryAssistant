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
        if ((cableService.findByName(cable.getName()))) {
            System.out.println("Кабель есть в базе");
            return true;
        } else
            System.out.println("Кабеля нет в базе");
        return false;

    }
}
