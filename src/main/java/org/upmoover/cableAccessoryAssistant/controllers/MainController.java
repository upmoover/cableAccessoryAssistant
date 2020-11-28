package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    //котроллер, возвращающий стартовую страницу
    @GetMapping("/")
    public String homepage() {
        return "index";
    }

    //котроллер, возвращающий страницу справки
    @GetMapping("/help")
    public String help() {
        return "help";
    }
}
