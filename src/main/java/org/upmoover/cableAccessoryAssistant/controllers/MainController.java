package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.upmoover.cableAccessoryAssistant.entities.Cable;
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
        ArrayList<Cable> cables = CableFileReader.readFile(pathFile);
        model.addAttribute("cables", cables);
        return "show-cables-for-selection-accessories";
    }

    @RequestMapping("/start/get-attributes")
    @ResponseStatus(value = HttpStatus.OK)
    public void getCableAttributes(@ModelAttribute("selectedOption") String selectedOption) {
        System.out.println(selectedOption);
    }

}
