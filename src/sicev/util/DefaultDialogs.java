/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Moacir
 */
public class DefaultDialogs extends Alert{
    
    public DefaultDialogs(AlertType alertType) {
        super(alertType);
    }
    
    public void showAlert(Alert.AlertType tipo, String texto) {
        Alert alert = new Alert(tipo);
        alert.setTitle("SICEV");
        alert.setHeaderText("Produtos");
        alert.setContentText(texto);
        
        alert.show();
    }
    
    public ButtonBar.ButtonData confirmAlert(String texto) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("SICEV");
        alert.setHeaderText("Produtos");
        alert.setContentText(texto);
        alert.getButtonTypes().setAll( 
                new ButtonType("Sim", ButtonBar.ButtonData.YES), 
                new ButtonType("Não", ButtonBar.ButtonData.NO) );
        
        return alert.showAndWait().get().getButtonData();
    }
}
