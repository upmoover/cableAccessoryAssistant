package org.upmoover.cableAccessoryAssistant.utils;

import org.upmoover.cableAccessoryAssistant.entities.Cable;

import java.util.ArrayList;

public class Corrections {
    public static Float makeCorrectionByStandardSize(Float cableOuterDiameter, Float min, Float max, Float correction) {
        if (min < ((cableOuterDiameter / 100) * correction) && ((cableOuterDiameter / 100) * correction) < max)
            return (cableOuterDiameter / 100) * correction;
        else return (max - min) / 2;
    }
}
