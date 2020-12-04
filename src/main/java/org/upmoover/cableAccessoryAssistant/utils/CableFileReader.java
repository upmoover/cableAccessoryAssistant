package org.upmoover.cableAccessoryAssistant.utils;

import org.upmoover.cableAccessoryAssistant.entities.Cable;

import java.io.*;
import java.util.ArrayList;

public class CableFileReader {

    public static ArrayList<Cable> readFile(String filePath) {
        ArrayList<Cable> cables = new ArrayList<>();
        String str = null;
        String[] arr = new String[10];
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-16"))) {

            while ((str = br.readLine()) != null) {
                arr = str.split("\t");
                cables.add(new Cable((arr[0] + arr[1]).replace(',', '.'), Float.parseFloat(arr[2].replace(',', '.')), Float.parseFloat(arr[3].replace(',', '.'))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Cable cable: cables
             ) {
            System.out.println(cable);
        }
        return cables;
    }


}
