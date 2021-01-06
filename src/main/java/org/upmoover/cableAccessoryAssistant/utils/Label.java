package org.upmoover.cableAccessoryAssistant.utils;

public class Label {

    private static Label instance;
    public int counter = 0;

    private Label() {
    }

    public static Label getInstance() {
        if (instance == null) instance = new Label();
        return instance;
    }
}
