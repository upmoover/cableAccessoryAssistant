package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandPG;
import org.upmoover.cableAccessoryAssistant.services.CableGlandPgService;

import java.util.ArrayList;

@Controller
@RequestMapping("/database/cableGlandPg")
public class CableGlandPgController {

    CableGlandPgService cableGlandPgService;

    @Autowired
    public void setCableGlandPgService(CableGlandPgService cableGlandPgService) {
        this.cableGlandPgService = cableGlandPgService;
    }

    //отобразить страницу редактирования БД кабельного ввода PG
    @RequestMapping("")
    public String showFormEditCableGlandPg() {
        return "edit-cableGlandPg-database";
    }

    //отобразить страницу добавления в БД кабельного ввода PG
    @RequestMapping("/add-form")
    public String showAddCableGlandPg() {
        return "one-cableGlandPg-add-form";
    }

    //добавить в БД кабельный ввод PG, отобразить страницу добавления в БД кабельного ввода PG
    @RequestMapping("/add")
    public String addCableGlandPg(@RequestParam String name, String minDiameter, String maxDiameter, String vendorCode) {
        cableGlandPgService.saveOneCableGlandPgToBase(new CableGlandPG(name, Float.parseFloat(maxDiameter.replace(',', '.')), vendorCode, Float.parseFloat(minDiameter.replace(',', '.'))));
        return "redirect:/database/cableGlandPg/show-all-from-base";
    }

    //отобразить страницу с полным списком кабельного ввода PG из БД
    @RequestMapping("/show-all-from-base")
    public String editCableGlandPg(Model model) {
        ArrayList<CableGlandPG> cableGlandPGS;
        cableGlandPGS = cableGlandPgService.findAllFromBase();
        model.addAttribute("cableGlandPgS", cableGlandPGS);
        return "show-all-cableGlandPg-from-base";
    }

    //удалить из базы выбранный кабель и возвращающий обратно на страницу с кабелями
    @RequestMapping("/delete-cableGlandPg-by-id/{id}")
    public String deleteCableGlandPgById(@PathVariable("id") Long id) {
        cableGlandPgService.deleteCableGlandPgById(id);
        return "redirect:/database/cableGlandPg/show-all-from-base";
    }
}
