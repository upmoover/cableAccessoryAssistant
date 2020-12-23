package org.upmoover.cableAccessoryAssistant.utils;

import org.upmoover.cableAccessoryAssistant.entities.Cable;

import java.io.*;
import java.util.ArrayList;

public class CableFileReader {

    public static ArrayList<Cable> readFile(String filePath) {
        ArrayList<Cable> cables = new ArrayList<>();
        String str;
        String[] arr;
        int countField;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-16"))) {
            Cable cable;
            while ((str = br.readLine()) != null) {
                countField = 0;
                arr = str.split("\t");

                for (int i = 0; i < arr.length; i++) {
                    if (!arr[i].equals("")) countField++;
                }

                if (countField == 4) {
                    cable = new Cable((arr[0] + " " + arr[1]).replace(',', '.'), Float.parseFloat(arr[2].replace(',', '.')), Float.parseFloat(arr[3].replace(',', '.')));
                    //проверить кабель на уникальность относительно БД
                    if (!CheckUniqueness.isCableInTheBase(cable))
                        cables.add(cable);
                } else {
                    cable = new Cable(arr[0], arr[1], Float.parseFloat(arr[2].replace(',', '.')));
                    cables.add(cable);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return cables;
    }


}
