package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.upmoover.cableAccessoryAssistant.entities.Cable;
import org.upmoover.cableAccessoryAssistant.services.CableService;

import javax.persistence.Access;

@Controller
@RequestMapping("/database")
public class DatabaseController {

    CableService cableService;

    //внедрение зависимости для класса-сервиса кабеля
    @Autowired
    public void setCableService(CableService cableService) {
        this.cableService = cableService;
    }

    //контроллер, возвращающий страницу навигации для редактирования базы данных
    @RequestMapping("")
    public String editDatabase() {
        return "edit-database";
    }

    @RequestMapping("/showCableAddForm")
    public String showCableAddForm() {
        return "one-cable-add-form";
    }

    @RequestMapping("/showCableAddForm/addCableViaForm")
    public String addViaForm(@RequestParam String cableType, String numberOfWires, String sectionOfWire, String outerDiameter, String weight) {
        cableService.saveCableToBase(new Cable(cableType + numberOfWires + "х" + sectionOfWire, Double.parseDouble(outerDiameter.replace(',', '.')), Double.parseDouble(weight.replace(',', '.'))));
        return "show-all-cables-from-base";
    }
}
