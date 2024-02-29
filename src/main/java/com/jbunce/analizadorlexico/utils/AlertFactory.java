package com.jbunce.analizadorlexico.utils;

import javafx.scene.control.Alert;

public class AlertFactory {

    public static void makeAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
