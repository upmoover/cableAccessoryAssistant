package org.upmoover.cableAccessoryAssistant.utils;

import org.upmoover.cableAccessoryAssistant.entities.Cable;

import java.io.*;
import java.util.ArrayList;

public class CableFileReader {

    public static ArrayList<Cable> readFile(String filePath) {
        ArrayList<Cable> cables = new ArrayList<>();
        String str;
        String[] arr;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-16"))) {
            Cable cable;
            while ((str = br.readLine()) != null) {
                arr = str.split("\t");
                //если строка не начинается с имени кабеля в проекте, значит в файле содержится список кабеля для добавления в базу данных
                if (!arr[0].contains("W")) {
                    cable = new Cable((arr[0] + " " + arr[1]).replace(',', '.'), Float.parseFloat(arr[2].replace(',', '.')), Float.parseFloat(arr[3].replace(',', '.')));
                    //проверить кабель на уникальность относительно БД
                    if (!CheckUniqueness.isCableInTheBase(cable))
                        cables.add(cable);
                } else {
                    //иначе, в файле содержится список кабеля для подбора аксессуаров
                    String rplc = arr[2].replace(" m", "");
                    rplc = rplc.replace(",", ".");
                    cable = new Cable(arr[0], arr[1], Float.parseFloat(rplc));
                    cables.add(cable);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cables;
    }

}
