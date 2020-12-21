package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandRgg;
import org.upmoover.cableAccessoryAssistant.services.CableGlandRggService;

import java.util.ArrayList;

@Controller
@RequestMapping("/database/cableGlandRgg")
public class CableGlandRggController {

    CableGlandRggService CableGlandRggService;

    @Autowired
    public void setCableGlandRggService(CableGlandRggService CableGlandRggService) {
        this.CableGlandRggService = CableGlandRggService;
    }

    //отобразить страницу редактирования БД кабельного ввода PG
    @RequestMapping("")
    public String showFormEditCableGlandRgg() {
        return "edit-cableGlandRgg-database";
    }

    //отобразить страницу добавления в БД кабельного ввода Rgg
    @RequestMapping("/add-form")
    public String showAddCableGlandRgg() {
        return "one-CableGlandRgg-add-form";
    }

    //добавить в БД кабельный ввод PG, отобразить страницу добавления в БД кабельного ввода PG
    @RequestMapping("/add")
    public String addCableGlandRgg(@RequestParam String name, String minDiameter, String maxDiameter, String vendorCode) {
        CableGlandRggService.saveOneCableGlandRggToBase(new CableGlandRgg(name, Float.parseFloat(maxDiameter.replace(',', '.')), vendorCode, Float.parseFloat(minDiameter.replace(',', '.'))));
        return "redirect:/database/CableGlandRgg/show-all-from-base";
    }

    //отобразить страницу с полным списком кабельного ввода PG из БД
    @RequestMapping("/show-all-from-base")
    public String editCableGlandPg(Model model) {
        ArrayList<CableGlandRgg> CableGlandRggS;
        CableGlandRggS = CableGlandRggService.findAllFromBase();
        model.addAttribute("CableGlandRggS", CableGlandRggS);
        return "show-all-cableGlandRgg-from-base";
    }

    //удалить из базы выбранный кабель и возвращающий обратно на страницу с кабелями
    @RequestMapping("/delete-CableGlandRgg-by-id/{id}")
    public String deleteCableGlandRggById(@PathVariable("id") Long id) {
        CableGlandRggService.deleteCableGlandRggById(id);
        return "redirect:/database/CableGlandRgg/show-all-from-base";
    }
}
