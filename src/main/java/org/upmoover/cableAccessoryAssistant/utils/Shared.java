package org.upmoover.cableAccessoryAssistant.utils;

import org.upmoover.cableAccessoryAssistant.entities.Cable;

import java.util.ArrayList;
import java.util.HashSet;

public class Shared {

    public static ArrayList<Cable> notFoundCables;
    public static ArrayList<Cable> unknownCables = new ArrayList<>();
    public static HashSet<Cable> uniqueNotFoundCables = new HashSet<>();
    public static ArrayList<Cable> cablesFoundInBase = new ArrayList<>();
    public static boolean isUnknown;
    public static boolean isSkipCablesSelected;
    public static ArrayList<Cable> listCablesFromFile = new ArrayList<>();

    public Shared() {
    }

}
