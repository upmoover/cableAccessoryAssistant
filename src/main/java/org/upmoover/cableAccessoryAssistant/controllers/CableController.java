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
import org.upmoover.cableAccessoryAssistant.repositories.CableRepository;
import org.upmoover.cableAccessoryAssistant.services.CableService;
import org.upmoover.cableAccessoryAssistant.utils.CableFileReader;
import org.upmoover.cableAccessoryAssistant.utils.CheckUniqueness;

import java.util.ArrayList;

@Controller
@RequestMapping("/database/cable")
public class CableController {
    CableRepository cableRepository;

    @Autowired
    public void setCableRepository(CableRepository cableRepository) {
        this.cableRepository = cableRepository;
    }

    CableService cableService;

    //внедрение зависимости для класса-сервиса кабеля
    @Autowired
    public void setCableService(CableService cableService) {
        this.cableService = cableService;
    }

    //отобразить страницу формы добавления кабеля
    @RequestMapping("/show-cable-add-form")
    public String showCableAddForm() {
        return "one-cable-add-form";
    }

    //сохранить кабель в базу данных из формы
    @RequestMapping("/showCableAddForm/addCableViaForm")
    public String saveOneCableToBase(@RequestParam String cableType, String numberOfWires, String sectionOfWire, String outerDiameter, String weight, String numberOfWiresSecond, String sectionOfWireSecond) {

        Cable cable;

        if (numberOfWiresSecond.equals("") || sectionOfWireSecond.equals(""))
            cable = new Cable(cableType + " " + numberOfWires + "х" + sectionOfWire, Float.parseFloat(outerDiameter.replace(',', '.')), Float.parseFloat(weight.replace(',', '.')));
        else cable = new Cable(cableType + " " + numberOfWires + "х" + sectionOfWire + "+" + numberOfWiresSecond + "х" + sectionOfWireSecond, Float.parseFloat(outerDiameter.replace(',', '.')), Float.parseFloat(weight.replace(',', '.')));
        if (!CheckUniqueness.isCableInTheBase(cable)) {
                cableService.saveOneCableToBase(cable);
        }
        return "redirect:/database/cable/show-cable-add-form";
        //TODO при добавлении одиночного кабеля назначить id соответствующего кабельного ввода
    }

    //отобразить все кабели из базы (с возможностью удаления выбранного кабеля)
    @RequestMapping("/show-all-cables-from-base")
    public String showAllCablesFromBase(Model model) {
        ArrayList<Cable> cables;
        cables = cableService.findAllFromBase();
        model.addAttribute("cables", cables);
        return "show-all-cables-from-base";
    }

    //удалить из базы выбранный кабель и возвращающий обратно на страницу с кабелями
    @RequestMapping("/delete-cable-by-id/{id}")
    public String deleteCabelById(@PathVariable("id") Long id) {
        cableService.deleteCableById(id);
        return "redirect:/database/cable/show-all-cables-from-base";
    }

    //отобразить страницу добавления кабелей в базу данных список кабелей из txt файла
    @RequestMapping("/show-cable-add-from-file-form")
    public String showFormSaveFromFile() {
        return "add-cable-from-file";
    }

    //добавить кабели в базу данных список кабелей из txt файла
    @RequestMapping("/file-path")
    @ResponseStatus(value = HttpStatus.OK)
    public void addCableFromFile(@RequestParam String pathFile) {
        cableService.saveCableToBase(CableFileReader.readFile(pathFile));

        //TODO сделать проверку на уникальность добавляемого кабеля в БД (чтобы избежать дубликатов кабеля)
        //TODO при добавлении из файла назначить id соответствующего кабельного ввода
    }

    //отобразить страницу редактирования базы данных кабелей
    @RequestMapping("/edit-cable-database")
    public String showEditCablePage() {
        return "edit-cable-database";
    }

}
