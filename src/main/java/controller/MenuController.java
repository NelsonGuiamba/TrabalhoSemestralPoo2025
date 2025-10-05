/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

/**
 *
 * @author Administrator
 */
public class MenuController {
    private ImageView btNext;
    
    @FXML
    private void initialize(){
    btNext.setOnMouseEntered(e -> btNext.getScene().setCursor(javafx.scene.Cursor.HAND));
    btNext.setOnMouseExited(e -> btNext.getScene().setCursor(javafx.scene.Cursor.DEFAULT));
    }
}
