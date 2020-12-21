package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandMG;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandPG;
import org.upmoover.cableAccessoryAssistant.services.CableGlandMgService;
import org.upmoover.cableAccessoryAssistant.services.CableGlandPgService;

import java.util.ArrayList;
@Controller
@RequestMapping("/database/cableGlandMg")
public class CableGlandMgController {

    CableGlandMgService cableGlandMgService;

    @Autowired
    public void setCableGlandMgService(CableGlandMgService cableGlandMgService) {
        this.cableGlandMgService = cableGlandMgService;
    }

    //отобразить страницу редактирования БД кабельного ввода PG
    @RequestMapping("")
    public String showFormEditCableGlandMg() {
        return "edit-cableGlandMg-database";
    }

    //отобразить страницу добавления в БД кабельного ввода PG
    @RequestMapping("/add-form")
    public String showAddCableGlandMg() {
        return "one-cableGlandMg-add-form";
    }

    //добавить в БД кабельный ввод PG, отобразить страницу добавления в БД кабельного ввода PG
    @RequestMapping("/add")
    public String addCableGlandMg(@RequestParam String name, String minDiameter, String maxDiameter, String vendorCode) {
        cableGlandMgService.saveOneCableGlandMgToBase(new CableGlandMG(name, Float.parseFloat(maxDiameter.replace(',', '.')), vendorCode, Float.parseFloat(minDiameter.replace(',', '.'))));
        return "redirect:/database/cableGlandMg/show-all-from-base";
    }

    //отобразить страницу с полным списком кабельного ввода PG из БД
    @RequestMapping("/show-all-from-base")
    public String editCableGlandPg(Model model) {
        ArrayList<CableGlandMG> cableGlandMGS;
        cableGlandMGS = cableGlandMgService.findAllFromBase();
        model.addAttribute("cableGlandMgS", cableGlandMGS);
        return "show-all-cableGlandMg-from-base";
    }

    //удалить из базы выбранный кабель и возвращающий обратно на страницу с кабелями
    @RequestMapping("/delete-cableGlandMg-by-id/{id}")
    public String deleteCableGlandMgById(@PathVariable("id") Long id) {
        cableGlandMgService.deleteCableGlandMgById(id);
        return "redirect:/database/cableGlandMg/show-all-from-base";
    }
}
