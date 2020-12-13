package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandPG;
import org.upmoover.cableAccessoryAssistant.repositories.CableGlandPgRepository;

@Controller
@RequestMapping("/database/cableGlandPg")
public class CableGlandPgController {

    CableGlandPgRepository cableGlandPgRepository;

    @Autowired
    public void setCableGlandPgRepository(CableGlandPgRepository cableGlandPgRepository) {
        this.cableGlandPgRepository = cableGlandPgRepository;
    }

    @RequestMapping("")
    public String showFormEditCableGlandPg() {
        return "edit-cableGlandPg-database";
    }

    @RequestMapping("/add-form")
    public String showAddCableGlandPg() {
        return "one-cableGlandPg-add-form";
    }

    @RequestMapping("/add")
    public String addCableGlandPg(@RequestParam String name, String minDiameter, String maxDiameter, String vendorCode) {
        cableGlandPgRepository.save(new CableGlandPG(name, Long.parseLong(minDiameter.replace(',', '.')), vendorCode, Long.parseLong(maxDiameter.replace(',', '.'))));
        return "/database/cableGlandPg/show-all-from-base";
    }

    @RequestMapping("/show-all-from-base")
    public String editCableGlandPg() {
        return "edit-cableGlandPg-database";
    }

}
