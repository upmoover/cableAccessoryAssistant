package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.upmoover.cableAccessoryAssistant.utils.CableFileReader;

@Controller
public class MainController {

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
    public String start() { return "start"; }

    //считать список кабеля, вывести страницу со списком и выбором аксессуаров
    @GetMapping("/start/show-cables")
    public String showCableList(@RequestParam String pathFile){
        CableFileReader.readFile(pathFile);
        return "help";
    }

}
