package org.upmoover.cableAccessoryAssistant.utils;

import org.upmoover.cableAccessoryAssistant.entities.Cable;

import java.io.*;
import java.util.ArrayList;

public class CableFileReader {

    public static ArrayList<Cable> readFile(String filePath) {
        ArrayList<Cable> cables = new ArrayList<>();
        File file = new File(filePath);
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {

            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            System.out.println("Файл не найден!");
            e.printStackTrace();
        }
        return cables;
    }



}
