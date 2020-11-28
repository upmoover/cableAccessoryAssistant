package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/database")
public class DatabaseController {

    @RequestMapping("/database")
    public String editDatabase() {
        return "editDatabase";
    }
}
