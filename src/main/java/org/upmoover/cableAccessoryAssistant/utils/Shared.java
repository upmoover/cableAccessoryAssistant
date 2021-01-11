package org.upmoover.cableAccessoryAssistant.utils;

import org.upmoover.cableAccessoryAssistant.entities.Cable;

import java.util.ArrayList;
import java.util.HashSet;

public class Shared {

    public static ArrayList<Cable> notFoundCables;
    public static HashSet<Cable> uniqueNotFoundCables = new HashSet<>();

    public Shared() {
    }

}
