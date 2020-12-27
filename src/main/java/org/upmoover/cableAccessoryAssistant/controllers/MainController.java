package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.upmoover.cableAccessoryAssistant.entities.Cable;
import org.upmoover.cableAccessoryAssistant.services.CableService;
import org.upmoover.cableAccessoryAssistant.utils.CableFileReader;

import java.util.ArrayList;

@Controller
public class MainController {

    Cable cable = new Cable("КГ 1х2.5", 6.7f, 80.0f);
    Cable cable1 = new Cable("КГ 1х4", 6.7f, 80.0f);
    Cable cable2 = new Cable("КГ 1х6", 6.7f, 80.0f);
    Cable cable3 = new Cable("КГ 1х10", 6.7f, 80.0f);
    Cable cable4 = new Cable("КГ 1х16", 6.7f, 80.0f);
    Cable cable5 = new Cable("КГ 1х25", 6.7f, 80.0f);

    public ArrayList<Cable> cables = new ArrayList<>();

    CableService cableService;

    @Autowired
    public void setCableService(CableService cableService) {
        this.cableService = cableService;
    }

    //вернуть стартовую страницу
    @GetMapping("/")
    public String homepage() {
        return "index";
    }

    //вернуть страницу справки
    @GetMapping("/help")
    public String help() {
        return "help";
    }

    //вернуть страницу добавления списка кабелей для подбора аксессуаров
    @GetMapping("/start")
    public String start() {
        return "start";
    }

    //считать список кабеля, вывести страницу со списком и выбором аксессуаров
    @GetMapping("/start/show-cables")
    @ResponseStatus(value = HttpStatus.OK)
    public void showCableList(@RequestParam String pathFile) {
        cables.add(cable);
        cables.add(cable1);
        cables.add(cable2);
        cables.add(cable3);
        cables.add(cable4);
        cables.add(cable5);
        for (int i = 0; i < cables.size(); i++) {
            String cbl = cableService.findCableByName(cables.get(i).getName()).getName();
            System.out.println(cableService.findCableByName(cables.get(i).getName()).getCableGlandMg().getName());
        }
    }

}
