/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.controller;

import sicev.view.SICEV;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

/**
 * FXML Controller class
 *
 * @author Moacir
 */
public class MainController implements Initializable {

    private SICEV application;
    
    @FXML
    private MenuItem btnMenuProdutos;
    @FXML
    private MenuItem btnMenuClientes;
    
    @FXML
    private void handleBtnMenuProdutosAction (ActionEvent  event) {
        this.application.openProdutos();
    }
    
    @FXML
    private void handleBtnMenuClientesAction (ActionEvent  event) {
        this.application.openClientes();
    }
    
    @FXML
    private void handleBtnMenuUsuariosAction (ActionEvent  event) {
        this.application.openUsuarios();
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    public void setApp(SICEV application) {
        this.application = application;
    }
    
}
