package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.upmoover.cableAccessoryAssistant.entities.Cable;
import org.upmoover.cableAccessoryAssistant.services.CableService;
import org.upmoover.cableAccessoryAssistant.utils.CableFileReader;

import java.util.ArrayList;

@Controller
@RequestMapping("/database")
public class DatabaseController {

    CableService cableService;

    //внедрение зависимости для класса-сервиса кабеля
    @Autowired
    public void setCableService(CableService cableService) {
        this.cableService = cableService;
    }

    //контроллер, возвращающий страницу навигации для редактирования базы данных
    @RequestMapping("")
    public String editDatabase() {
        return "edit-database";
    }

    //контроллер, отображающий страницу формы добавления кабеля
    @RequestMapping("/show-cable-add-form")
    public String showCableAddForm() {
        return "one-cable-add-form";
    }

    //контроллер, сохраняющий кабель в базу данных из формы
    @RequestMapping("/showCableAddForm/addCableViaForm")
    public String saveOneCableToBase(@RequestParam String cableType, String numberOfWires, String sectionOfWire, String outerDiameter, String weight) {
        cableService.saveOneCableToBase(new Cable(cableType + " " + numberOfWires + "х" + sectionOfWire, Float.parseFloat(outerDiameter.replace(',', '.')), Float.parseFloat(weight.replace(',', '.'))));
        return "redirect:/database/show-cable-add-form";
    }

    //контроллер, отображающий все кабели из базы (с возможностью удаления выбранного кабеля)
    @RequestMapping("/show-all-cables-from-base")
    public String showAllCablesFromBase(Model model) {
        ArrayList<Cable> cables = new ArrayList<>();
        cables = cableService.findAllFromBase();
        model.addAttribute("cables", cables);
        return "show-all-cables-from-base";
    }

    //контроллер, удаляющий из базы выбранный кабель и возвращающий обратно на страницу с кабелями
    @RequestMapping("/delete-cable-by-id/{id}")
    public String deleteCabelById(@PathVariable("id") Long id) {
        cableService.deleteCableById(id);
        return "redirect:/database/show-all-cables-from-base";
    }

    //контроллер, добавляющий кабели в базу данных список кабелей из txt файла
    @RequestMapping("/show-cable-add-from-file-form")
    public String showFormSaveFromFile() {
        return "/add-cable-from-file";
    }

    @RequestMapping("/file-path")
    @ResponseStatus(value = HttpStatus.OK)
    public void addCableFromFile(@RequestParam String pathFile) {
        CableFileReader.readFile(pathFile);
//        cableService.saveCableToBase();
    }
}
