package org.upmoover.cableAccessoryAssistant.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.upmoover.cableAccessoryAssistant.entities.Cable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExcelUtil {
    public static void writeExcelFile(String path, HashMap<String, ArrayList<String>> locationList, ArrayList<String> cablesWithLength, ArrayList<Cable> cablesWithDesignatedAccessories) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet accessoriesByLocation = workbook.createSheet("Аксессуары по местоположению");
        int i = 0;
        int j;
        for (String location : locationList.keySet()
        ) {
            accessoriesByLocation.createRow(i).createCell(0).setCellValue(location);
            i++;

//            List<String> accessories = Arrays.asList(locationList.get(location).toString().split(","));
            List<String> accessories = new ArrayList<>();

            for (int k = 0; k < locationList.get(location).size(); k++) {
                accessories.add(locationList.get(location).get(k));
            }
            for (j = 0; j < accessories.size(); j++) {
                Row row = accessoriesByLocation.createRow(i);
                /*if (accessories.get(j).split("=")[0].contains("["))
                    row.createCell(0).setCellValue(accessories.get(j).split("=")[0].replace("[", ""));
                else*/
                row.createCell(0).setCellValue(accessories.get(j).split("=")[0]);

                /*if (accessories.get(j).split("=")[1].contains("]"))
                    row.createCell(1).setCellValue(accessories.get(j).split("=")[1].replace("]", ""));
                else*/
                row.createCell(1).setCellValue(accessories.get(j).split("=")[1]);
                i++;
            }
        }

        Sheet summedCables = workbook.createSheet("Суммированные кабели");
        int n = 0;
        int m;
        for (m = 0; m < cablesWithLength.size(); m++) {
            Row row = summedCables.createRow(n);

            row.createCell(0).setCellValue(cablesWithLength.get(m).split("=")[0]);
            row.createCell(1).setCellValue(cablesWithLength.get(m).split("=")[1]);
            n++;
        }

        Sheet cablesWithAccessories = workbook.createSheet("Кабели с аксессуарами");
        int k = 0;
        int l;
        for (l = 0; l < cablesWithDesignatedAccessories.size(); l++) {
            Row row = cablesWithAccessories.createRow(l);
            int z = 0;
            row.createCell(z).setCellValue(cablesWithDesignatedAccessories.get(l).getDesignation());
            z++;
            row.createCell(z).setCellValue(cablesWithDesignatedAccessories.get(l).getName());
            z++;
            row.createCell(z).setCellValue("[" + cablesWithDesignatedAccessories.get(l).getStartLocation() + "]");
            z++;
            row.createCell(z).setCellValue("=");
            z++;
            row.createCell(z).setCellValue(cablesWithDesignatedAccessories.get(l).getSelectedCableGlandStart().getName());
            if (cablesWithDesignatedAccessories.get(l).getCorrugatedPipeStart() != null) {
                z++;
                row.createCell(z).setCellValue(cablesWithDesignatedAccessories.get(l).getCorrugatedPipeStart().getName() + "=" + cablesWithDesignatedAccessories.get(l).getCorrugatedPipeStartLength());
            }
            z++;
            row.createCell(z).setCellValue("--->");
            z++;
            row.createCell(z).setCellValue("[" + cablesWithDesignatedAccessories.get(l).getEndLocation() + "]");
            z++;
            row.createCell(z).setCellValue("=");
            z++;
            row.createCell(z).setCellValue(cablesWithDesignatedAccessories.get(l).getSelectedCableGlandEnd().getName());
            if (cablesWithDesignatedAccessories.get(l).getCorrugatedPipeEnd() != null) {
                z++;
                row.createCell(z).setCellValue(cablesWithDesignatedAccessories.get(l).getCorrugatedPipeEnd().getName() + "=" + cablesWithDesignatedAccessories.get(l).getCorrugatedPipeEndLength());
            }
        }

        FileOutputStream fis = new FileOutputStream(path);
        workbook.write(fis);
        workbook.close();
        fis.close();
    }
}