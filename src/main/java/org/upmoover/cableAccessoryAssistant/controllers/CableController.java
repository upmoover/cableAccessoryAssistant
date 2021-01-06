package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.upmoover.cableAccessoryAssistant.entities.*;
import org.upmoover.cableAccessoryAssistant.repositories.*;
import org.upmoover.cableAccessoryAssistant.services.CableService;
import org.upmoover.cableAccessoryAssistant.utils.CableFileReader;
import org.upmoover.cableAccessoryAssistant.utils.CheckUniqueness;
import org.upmoover.cableAccessoryAssistant.utils.Label;

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

    CableGlandMgRepository cableGlandMgRepository;

    @Autowired
    public void setCableGlandMgRepository(CableGlandMgRepository cableGlandMgRepository) {
        this.cableGlandMgRepository = cableGlandMgRepository;
    }

    CorrugatedPipeRepository corrugatedPipeRepository;

    @Autowired
    public void setCorrugatedPipeRepository(CorrugatedPipeRepository corrugatedPipeRepository) {
        this.corrugatedPipeRepository = corrugatedPipeRepository;
    }

    CableGlandRggRepository cableGlandRggRepository;

    @Autowired
    public void setCableGlandRggRepository(CableGlandRggRepository cableGlandRggRepository) {
        this.cableGlandRggRepository = cableGlandRggRepository;
    }

    CableGlandPgRepository cableGlandPgRepository;

    @Autowired
    public void setCableGlandPgRepository(CableGlandPgRepository cableGlandPgRepository) {
        this.cableGlandPgRepository = cableGlandPgRepository;
    }

    //отобразить страницу формы добавления кабеля
    @RequestMapping("/show-cable-add-form")
    public String showCableAddForm() {
        Label label = Label.getInstance();
        label.counter = 1;//если пользователь попал на страницу добавления кабеля из этого контроллера, метка равна 1
        return "one-cable-add-form";
    }

    //сохранить кабель в базу данных из формы
    @RequestMapping("/showCableAddForm/addCableViaForm")
    public String saveOneCableToBase(@RequestParam String cableType, String numberOfWires, String sectionOfWire, String outerDiameter, String weight, String numberOfWiresSecond, String sectionOfWireSecond) {

        Cable cable;

        if (numberOfWiresSecond.equals("") || sectionOfWireSecond.equals(""))
            cable = new Cable(cableType + " " + numberOfWires + "х" + sectionOfWire, Float.parseFloat(outerDiameter.replace(',', '.')), Float.parseFloat(weight.replace(',', '.')));
        else
            cable = new Cable(cableType + " " + numberOfWires + "х" + sectionOfWire + "+" + numberOfWiresSecond + "х" + sectionOfWireSecond, Float.parseFloat(outerDiameter.replace(',', '.')), Float.parseFloat(weight.replace(',', '.')));
        if (!CheckUniqueness.isCableInTheBase(cable)) {
            cableService.saveOneCableToBase(cable);
        }
//        if (label.counter == 0)
//            return "redirect:/database/cable/show-cable-add-form";
//        else
            return "redirect:/start/add-notFoundCable";
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
    public String deleteCableById(@PathVariable("id") Long id) {
        cableService.deleteCableById(id);
        return "redirect:/database/cable/show-all-cables-from-base";
    }

    //отобразить страницу добавления кабелей в базу данных список кабелей из txt файла
    @RequestMapping("/show-cable-add-from-file-form")
    public String showFormSaveFromFile() {
        return "add-cable-from-file";
    }

    //сохранить кабели в базу данных список кабелей из txt файла
    @RequestMapping("/file-path")
    @ResponseStatus(value = HttpStatus.OK)
    public void addCableFromFile(@RequestParam String pathFile) {
        //получение списка кабелей из файла для добавления в базу данных
        ArrayList<Cable> cables = CableFileReader.readFile(pathFile);
        //проход по полученному списку кабелей, для подбора к каждому из кабелей аксессуаров из соответствующих таблиц
        for (int i = 0; i < cables.size(); i++) {
            //подбор аксессуара: кабельный ввод PG
            CableGlandPG cableglandpg = cableGlandPgRepository.findFirstByMaxDiameterGreaterThanEqualAndMinDiameterLessThanEqual(cables.get(i).getOuterDiameter(), cables.get(i).getOuterDiameter());
            cables.get(i).setCableGlandPg(cableglandpg);
            CableGlandMG cableGlandMG = cableGlandMgRepository.findFirstByMaxDiameterGreaterThanAndMinDiameterLessThanEqual(cables.get(i).getOuterDiameter(), cables.get(i).getOuterDiameter());
            cables.get(i).setCableGlandMg(cableGlandMG);
            CableGlandRgg cableGlandRgg = cableGlandRggRepository.findFirstByMaxDiameterGreaterThan(cables.get(i).getOuterDiameter());
            cables.get(i).setCableGlandRgg(cableGlandRgg);
            CorrugatedPipe corrugatedPipe = corrugatedPipeRepository.findFirstByInnerDiameterGreaterThan(cables.get(i).getOuterDiameter());
            cables.get(i).setCorrugatedPipe(corrugatedPipe);
            cableService.saveOneCableToBase(cables.get(i));
        }
        System.out.println("Кабели из файла добавлены в базу.");
    }

    //отобразить страницу редактирования базы данных кабелей
    @RequestMapping("/edit-cable-database")
    public String showEditCablePage() {
        return "edit-cable-database";
    }

    //удалить все записи из базы данных кабелей
    @RequestMapping("delete-all-cables-from-base")
    public String deleteAllFromBase() {
        cableRepository.deleteAll();
        return "redirect:/database/cable/show-all-cables-from-base";
    }

}
