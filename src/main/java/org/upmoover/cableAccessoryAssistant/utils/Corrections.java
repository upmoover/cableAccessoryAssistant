package org.upmoover.cableAccessoryAssistant.utils;

public class Corrections {
    public static Float makeCorrectionByStandardSize(Float cableOuterDiameter, Float min, Float max, Float correction) {
        if (correction.equals(0)) return 0F;
        if (min < ((cableOuterDiameter / 100) * correction) && ((cableOuterDiameter / 100) * correction) < max)
            return (cableOuterDiameter / 100) * correction;
        else return (max - min) / 2;
    }
}
