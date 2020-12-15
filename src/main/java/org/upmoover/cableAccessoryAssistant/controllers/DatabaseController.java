package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/database")
public class DatabaseController {

    //вернуть страницу навигации для редактирования базы данных
    @RequestMapping("")
    public String editDatabase() {
        return "edit-database";
    }

}
