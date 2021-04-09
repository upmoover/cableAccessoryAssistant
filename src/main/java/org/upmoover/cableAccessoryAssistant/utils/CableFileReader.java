package org.upmoover.cableAccessoryAssistant.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.upmoover.cableAccessoryAssistant.entities.Cable;

import java.io.*;
import java.util.ArrayList;

public class CableFileReader {

    public static ArrayList<Cable> readFile(String filePath) {
        ArrayList<Cable> cables = new ArrayList<>();
        cables.clear();
        String str;
        String[] arr;

        /*try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-16"))) {
            Cable cable;
            while ((str = br.readLine()) != null) {
                arr = str.split("\t");
                //если строка не начинается с имени кабеля в проекте (W1, W2...), значит в файле содержится список кабеля для добавления в базу данных
                if (!arr[0].contains("W")) {
                    String name = arr[1].replace('x', 'х');
                    cable = new Cable((arr[0] + " " + arr[1]).replace(',', '.'), Float.parseFloat(arr[2].replace(',', '.')), Float.parseFloat(arr[3].replace(',', '.')));
                    //проверить кабель на уникальность относительно БД
                    if (!CheckUniqueness.isCableInTheBase(cable))
                        cables.add(cable);
                } else {
                    //иначе, в файле содержится список кабеля для подбора аксессуаров
                    String rplc = arr[2].replace(" m", "");
                    rplc = rplc.replace(",", ".");
                    String name = arr[1].replace('x', 'х');
                    cable = new Cable(arr[0], name.replace(',', '.'), Float.parseFloat(rplc));
                    cables.add(cable);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cables;
    }*/
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            Workbook workbook = new HSSFWorkbook(fileInputStream);

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
//                while (!row.getCell(0).getStringCellValue().equals(""))
                if (!row.getCell(2).getStringCellValue().equals(""))
                cables.add(new Cable(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue().replace('x', 'х').replace(',', '.'), Float.parseFloat(row.getCell(2).getStringCellValue().replace(" m", ""))));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cables;
    }

}
