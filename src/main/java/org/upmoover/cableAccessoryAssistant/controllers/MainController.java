package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.upmoover.cableAccessoryAssistant.entities.Cable;
import org.upmoover.cableAccessoryAssistant.entities.Location;
import org.upmoover.cableAccessoryAssistant.repositories.LocationsRepository;
import org.upmoover.cableAccessoryAssistant.services.CableService;
import org.upmoover.cableAccessoryAssistant.utils.CableFileReader;

import java.util.ArrayList;

@Controller
public class MainController {

    public ArrayList<Cable> cables = new ArrayList<>();

    CableService cableService;

    @Autowired
    public void setCableService(CableService cableService) {
        this.cableService = cableService;
    }

    LocationsRepository locationsRepository;

    @Autowired
    public void setLocationsRepository(LocationsRepository locationsRepository) {
        this.locationsRepository = locationsRepository;
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
    public String showCableList(@RequestParam String pathFile, Model model) {
        //получение списка кабелей для подбора аксессуаров из файла
        ArrayList<Cable> cables = CableFileReader.readFile(pathFile);
        model.addAttribute("cables", cables);
        //получение из базы списка местоположений
        ArrayList<Location> locations = (ArrayList<Location>) locationsRepository.findAll();
        model.addAttribute("locations", locations);
        return "show-cables-for-selection-accessories";
    }

    @PostMapping("/start/get-attributes")
    @ResponseStatus(value = HttpStatus.OK)
    public void getCableAttributes(@RequestParam(value = "cableGlandType", required = false) String[] cableGlandType, @RequestParam(value = "startLocation", required = false) String[] startLocations) {
        for (int i = 0; i < cableGlandType.length; i++) {
            System.out.println("name: " + cableGlandType[i].split("=")[0] + ", cableGland: " + cableGlandType[i].split("=")[1] + "location: " + startLocations[i].split("=")[0]);
        }

    }
}
