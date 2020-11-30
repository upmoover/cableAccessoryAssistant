package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/database")
public class DatabaseController {

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
    public String addViaForm() {

        return "show-all-cables-from-base";
    }
}
