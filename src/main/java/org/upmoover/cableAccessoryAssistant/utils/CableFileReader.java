package org.upmoover.cableAccessoryAssistant.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.upmoover.cableAccessoryAssistant.entities.Cable;
import org.upmoover.cableAccessoryAssistant.entities.Location;
import org.upmoover.cableAccessoryAssistant.repositories.LocationsRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("singleton")
public class CableFileReader {

    LocationsRepository locationsRepository;

    @Autowired
    public void setLocationsRepository(LocationsRepository locationsRepository) {
        this.locationsRepository = locationsRepository;
    }

    public CableFileReader() {
    }

    List<Location> locationList = new ArrayList<>();

    public ArrayList<Cable> readFile(String filePath) {
        ArrayList<Cable> cables = new ArrayList<>();

        cables.clear();
        Shared.unknownCables.clear();
        Shared.isUnknown = false;
        String str;
        String[] arr;

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            Workbook workbook = new HSSFWorkbook(fileInputStream);

            List<String> locationNameList = new ArrayList<>();

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {

                if (row.getCell(3).getStringCellValue().contains(" ")) {
                    if (!locationsRepository.findAll().stream().map(Location::getName).collect(Collectors.toList()).contains(row.getCell(3).getStringCellValue().substring(row.getCell(3).getStringCellValue().indexOf("+") + 1, row.getCell(3).getStringCellValue().indexOf(" "))) && !row.getCell(3).getStringCellValue().equals("")) {
                        locationsRepository.save(new Location(row.getCell(3).getStringCellValue().substring(row.getCell(3).getStringCellValue().indexOf("+") + 1, row.getCell(3).getStringCellValue().indexOf(" "))));
                    }
                }
                if (row.getCell(4).getStringCellValue().contains(" ")) {
                    if (!locationsRepository.findAll().stream().map(Location::getName).collect(Collectors.toList()).contains(row.getCell(4).getStringCellValue().substring(row.getCell(4).getStringCellValue().indexOf("+") + 1, row.getCell(4).getStringCellValue().indexOf(" "))) && !row.getCell(4).getStringCellValue().equals(""))
                        locationsRepository.save(new Location(row.getCell(4).getStringCellValue().substring(row.getCell(4).getStringCellValue().indexOf("+") + 1, row.getCell(4).getStringCellValue().indexOf(" "))));
                } else {
                    if (!locationsRepository.findAll().stream().map(Location::getName).collect(Collectors.toList()).contains(row.getCell(3).getStringCellValue().substring(row.getCell(3).getStringCellValue().lastIndexOf("+") + 1)) && !row.getCell(3).getStringCellValue().equals(""))
                        locationsRepository.save(new Location(row.getCell(3).getStringCellValue().substring(row.getCell(3).getStringCellValue().lastIndexOf("+") + 1)));
                    if (!locationsRepository.findAll().stream().map(Location::getName).collect(Collectors.toList()).contains(row.getCell(4).getStringCellValue().substring(row.getCell(4).getStringCellValue().lastIndexOf("+") + 1)) && !row.getCell(4).getStringCellValue().equals(""))
                        locationsRepository.save(new Location(row.getCell(4).getStringCellValue().substring(row.getCell(4).getStringCellValue().lastIndexOf("+") + 1)));
                }
            }

            for (Row row : sheet) {

                String startLocation;
                String endLocation;

                if (row.getCell(3).getStringCellValue().contains("")) startLocation = "none";
                if (row.getCell(3).getStringCellValue().contains(" "))
                    startLocation = row.getCell(3).getStringCellValue().substring(row.getCell(3).getStringCellValue().indexOf("+") + 1, row.getCell(3).getStringCellValue().indexOf(" "));
                else
                    startLocation = row.getCell(3).getStringCellValue().substring(row.getCell(3).getStringCellValue().lastIndexOf("+") + 1);

                if (row.getCell(4).getStringCellValue().contains("")) endLocation = "none";
                if (row.getCell(4).getStringCellValue().contains(" "))
                    endLocation = row.getCell(4).getStringCellValue().substring(row.getCell(4).getStringCellValue().indexOf("+") + 1, row.getCell(4).getStringCellValue().indexOf(" "));
                else
                    endLocation = row.getCell(4).getStringCellValue().substring(row.getCell(4).getStringCellValue().lastIndexOf("+") + 1);

                String cableLength = "0 m";
                String cableName;

                if (!row.getCell(2).getStringCellValue().equals("")) cableLength = row.getCell(2).getStringCellValue();

                if (row.getCell(1).getStringCellValue().equals("")) {
                    Shared.unknownCables.add(new Cable(row.getCell(0).getStringCellValue(), "none=" + row.getCell(0).getStringCellValue(), Float.parseFloat(cableLength.replace(" m", "").replace(',', '.')), startLocation, endLocation));
                    //TODO проверить пропуск кода ниже по достижению continue
                    continue;
                }

                cables.add(new Cable(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue().replace('x', 'х').replace(',', '.'), Float.parseFloat(cableLength.replace(" m", "").replace(',', '.')), startLocation, endLocation));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cables;
    }

}
