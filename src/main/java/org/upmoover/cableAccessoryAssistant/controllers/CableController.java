package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.upmoover.cableAccessoryAssistant.entities.*;
import org.upmoover.cableAccessoryAssistant.repositories.*;
import org.upmoover.cableAccessoryAssistant.services.CableService;
import org.upmoover.cableAccessoryAssistant.utils.CableFileReader;
import org.upmoover.cableAccessoryAssistant.utils.CheckUniqueness;
import org.upmoover.cableAccessoryAssistant.utils.Shared;

import java.util.ArrayList;

@Controller
@RequestMapping("/database/cable")
public class CableController {

    CableFileReader cableFileReader;

    Cable cable = null;

    @Autowired
    public void setCableFileReader(CableFileReader cableFileReader) {
        this.cableFileReader = cableFileReader;
    }

    CableRepository cableRepository;
    public ArrayList<Cable> cables;

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

    CorrugatedPipePlasticRepository corrugatedPipePlasticRepository;

    @Autowired
    public void setCorrugatedPipePlasticRepository(CorrugatedPipePlasticRepository corrugatedPipePlasticRepository) {
        this.corrugatedPipePlasticRepository = corrugatedPipePlasticRepository;
    }

    CorrugatedPipeMetalRepository corrugatedPipeMetalRepository;

    @Autowired
    public void setCorrugatedPipeMetalRepository(CorrugatedPipeMetalRepository corrugatedPipeMetalRepository) {
        this.corrugatedPipeMetalRepository = corrugatedPipeMetalRepository;
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
    public String showCableAddForm() {//если пользователь попал на страницу добавления кабеля из этого контроллера, метка равна 1
        return "one-cable-add-form";
    }

    //сохранить кабель в базу данных из формы
    @RequestMapping("/showCableAddForm/addCableViaForm")
    public String saveOneCableToBase(@RequestParam String cableType, String numberOfWires, String sectionOfWire, String outerDiameter, String weight, String numberOfWiresSecond, String sectionOfWireSecond) {

        if (Shared.notFoundCables != null && Shared.notFoundCables.size() > 0)
            Shared.notFoundCables.remove(0);

        Cable cable;

        if (numberOfWiresSecond == null || sectionOfWireSecond == null || numberOfWiresSecond.equals("") || sectionOfWireSecond.equals(""))
            cable = new Cable(cableType + " " + numberOfWires + "х" + sectionOfWire, Float.parseFloat(outerDiameter.replace(',', '.')), Float.parseFloat(weight.replace(',', '.')));
        else
            cable = new Cable(cableType + " " + numberOfWires + "х" + sectionOfWire + "+" + numberOfWiresSecond + "х" + sectionOfWireSecond, Float.parseFloat(outerDiameter.replace(',', '.')), Float.parseFloat(weight.replace(',', '.')));
        if (!CheckUniqueness.isCableInTheBase(cable)) {
            chooseAccessory(cable);
            cableService.saveOneCableToBase(cable);
        }
        return "redirect:/start/add-notFoundCable";
    }

    //отобразить все кабели из базы (с возможностью удаления выбранного кабеля)
    @RequestMapping("/show-all-cables-from-base")
    public String showAllCablesFromBase(Model model) {
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

    //сохранить кабели в базу данных список кабелей из excel файла
    @RequestMapping("/file-path")
    public String addCableFromFile(@RequestParam String pathFile) {
        //получение списка кабелей из файла для добавления в базу данных

        ArrayList<Cable> cables = cableFileReader.readFile(pathFile);
        //проход по полученному списку кабелей, для подбора к каждому из кабелей аксессуаров из соответствующих таблиц
        for (int i = 0; i < cables.size(); i++) {
            //подбор аксессуара: кабельный ввод PG
            CableGlandPG cableglandpg = cableGlandPgRepository.findFirstByMaxDiameterGreaterThanEqualAndMinDiameterLessThanEqual(cables.get(i).getOuterDiameter(), cables.get(i).getOuterDiameter());
            cables.get(i).setCableGlandPg(cableglandpg);
            CableGlandMG cableGlandMG = cableGlandMgRepository.findFirstByMaxDiameterGreaterThanAndMinDiameterLessThanEqual(cables.get(i).getOuterDiameter(), cables.get(i).getOuterDiameter());
            cables.get(i).setCableGlandMg(cableGlandMG);
            CableGlandRgg cableGlandRgg = cableGlandRggRepository.findFirstByMaxDiameterGreaterThan(cables.get(i).getOuterDiameter());
            cables.get(i).setCableGlandRgg(cableGlandRgg);
            CorrugatedPipePlastic corrugatedPipePlastic = corrugatedPipePlasticRepository.findFirstByInnerDiameterGreaterThan(cables.get(i).getOuterDiameter());
            cables.get(i).setCorrugatedPipePlastic(corrugatedPipePlastic);
            CorrugatedPipeMetal corrugatedPipeMetal = corrugatedPipeMetalRepository.findFirstByInnerDiameterGreaterThan(cables.get(i).getOuterDiameter());
            cables.get(i).setCorrugatedPipeMetal(corrugatedPipeMetal);
            cableService.saveOneCableToBase(cables.get(i));
        }

        System.out.println("Кабели из файла добавлены в базу");

        return "redirect:/database/cable/show-all-cables-from-base";
    }

    //контроллер для поиска и назначения аксессуаров для кабеля
    public void chooseAccessory(Cable cable) {
        CableGlandPG cableglandpg = cableGlandPgRepository.findFirstByMaxDiameterGreaterThanEqualAndMinDiameterLessThanEqual(cable.getOuterDiameter(), cable.getOuterDiameter());
        cable.setCableGlandPg(cableglandpg);
        CableGlandMG cableGlandMG = cableGlandMgRepository.findFirstByMaxDiameterGreaterThanAndMinDiameterLessThanEqual(cable.getOuterDiameter(), cable.getOuterDiameter());
        cable.setCableGlandMg(cableGlandMG);
        CableGlandRgg cableGlandRgg = cableGlandRggRepository.findFirstByMaxDiameterGreaterThan(cable.getOuterDiameter());
        cable.setCableGlandRgg(cableGlandRgg);
        CorrugatedPipePlastic corrugatedPipePlastic = corrugatedPipePlasticRepository.findFirstByInnerDiameterGreaterThan(cable.getOuterDiameter());
        cable.setCorrugatedPipePlastic(corrugatedPipePlastic);
        CorrugatedPipeMetal corrugatedPipeMetal = corrugatedPipeMetalRepository.findFirstByInnerDiameterGreaterThan(cable.getOuterDiameter());
        cable.setCorrugatedPipeMetal(corrugatedPipeMetal);
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

    @RequestMapping("delete-selected-cables")
    public String deleteSelectedCables(@RequestParam(value = "isSelected", required = false) long[] isSelected) {

        for (int i = 0; i < isSelected.length; i++) {
            cableRepository.deleteById(isSelected[i]);
        }
        return "redirect:/database/cable/show-all-cables-from-base";
    }

    @RequestMapping("/unknown/showAddCableViaForm")
    public ModelAndView showFormAddUnknownCable(ModelAndView modelAndView) {
        modelAndView.addObject("cableDesignation", Shared.unknownCables.get(0).getDesignation());
        modelAndView.setViewName("unknown-cable-add-form");
        return modelAndView;
    }

    @RequestMapping("/unknown/addCableViaForm")
    public String saveUnknownCableToBase(@RequestParam String cableName, String outerDiameter) {
        Shared.isUnknown = true;
        if (!Shared.unknownCables.isEmpty()) {

            Shared.unknownCables.get(0).setName(cableName);
            Shared.unknownCables.get(0).setOuterDiameter(Float.parseFloat(outerDiameter.replace(",", ".")));
            Shared.unknownCables.get(0).setOuterDiameterFromBase(Float.parseFloat(outerDiameter.replace(",", ".")));

            CableGlandPG cableglandpg = cableGlandPgRepository.findFirstByMaxDiameterGreaterThanEqualAndMinDiameterLessThanEqual(Shared.unknownCables.get(0).getOuterDiameter(), Shared.unknownCables.get(0).getOuterDiameter());
            Shared.unknownCables.get(0).setCableGlandPg(cableglandpg);
            CableGlandMG cableGlandMG = cableGlandMgRepository.findFirstByMaxDiameterGreaterThanAndMinDiameterLessThanEqual(Shared.unknownCables.get(0).getOuterDiameter(), Shared.unknownCables.get(0).getOuterDiameter());
            Shared.unknownCables.get(0).setCableGlandMg(cableGlandMG);
            CableGlandRgg cableGlandRgg = cableGlandRggRepository.findFirstByMaxDiameterGreaterThan(Shared.unknownCables.get(0).getOuterDiameter());
            Shared.unknownCables.get(0).setCableGlandRgg(cableGlandRgg);
            CorrugatedPipePlastic corrugatedPipePlastic = corrugatedPipePlasticRepository.findFirstByInnerDiameterGreaterThan(Shared.unknownCables.get(0).getOuterDiameter());
            Shared.unknownCables.get(0).setCorrugatedPipePlastic(corrugatedPipePlastic);
            CorrugatedPipeMetal corrugatedPipeMetal = corrugatedPipeMetalRepository.findFirstByInnerDiameterGreaterThan(Shared.unknownCables.get(0).getOuterDiameter());
            Shared.unknownCables.get(0).setCorrugatedPipeMetal(corrugatedPipeMetal);

            Shared.cablesFoundInBase.add(Shared.unknownCables.get(0));

            Shared.unknownCables.remove(0);
        }

        if (Shared.unknownCables.isEmpty()) Shared.isUnknown = false;

//        if (Shared.unknownCables.isEmpty())
        return "redirect:/start/skip-notFoundCable";
        /*else
            return "redirect:/start/add-notFoundCable";*/

    }

}
