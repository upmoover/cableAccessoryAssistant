package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.upmoover.cableAccessoryAssistant.entities.CorrugatedPipe;
import org.upmoover.cableAccessoryAssistant.entities.CorrugatedPipePlastic;
import org.upmoover.cableAccessoryAssistant.services.СorrugatedPlasticPipeService;

import java.util.ArrayList;

@Controller
@RequestMapping("/database/corrugatedPipe")
public class CorrugatedPipePlasticController {

    СorrugatedPlasticPipeService corrugatedPipePlasticService;

    @Autowired
    public void setCorrugatedPipeService(СorrugatedPlasticPipeService corrugatedPipePlasticService) {
        this.corrugatedPipePlasticService = corrugatedPipePlasticService;
    }

    //отобразить страницу редактирования БД кабельного ввода PG
    @RequestMapping("")
    public String showFormEditCorrugatedPipe() {
        return "edit-СorrugatedPipe-database";
    }

    //отобразить страницу добавления в БД кабельного ввода PG
    @RequestMapping("/add-form")
    public String showAddCorrugatedPipe() {
        return "one-СorrugatedPipe-add-form";
    }

    //добавить в БД кабельный ввод PG, отобразить страницу добавления в БД кабельного ввода PG
    @RequestMapping("/add")
    public String addCorrugatedPipe(@RequestParam String name, String innerDiameter, String vendorCode) {
        corrugatedPipePlasticService.saveOneСorrugatedPipePlasticToBase((CorrugatedPipePlastic) new CorrugatedPipe(name, Float.parseFloat(innerDiameter.replace(',', '.')), vendorCode));
        return "redirect:/database/corrugatedPipe/show-all-from-base";
    }

    //отобразить страницу с полным списком кабельного ввода PG из БД
    @RequestMapping("/show-all-from-base")
    public String editCableGlandPg(Model model) {
        ArrayList<CorrugatedPipePlastic> CorrugatedPipeS;
        CorrugatedPipeS = corrugatedPipePlasticService.findAllFromBase();
        model.addAttribute("CorrugatedPipeS", CorrugatedPipeS);
        return "show-all-CorrugatedPipe-from-base";
    }

    //удалить из базы выбранный кабельный ввод и вернуть обратно на страницу с кабельными вводами
    @RequestMapping("/delete-CorrugatedPipe-by-id/{id}")
    public String deleteCorrugatedPipeById(@PathVariable("id") Long id) {
        corrugatedPipePlasticService.deleteСorrugatedPipePlasticById(id);
        return "redirect:/database/corrugatedPipe/show-all-from-base";
    }
}
