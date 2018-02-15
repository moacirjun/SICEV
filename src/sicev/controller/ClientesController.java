/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sicev.view.SICEV;

/**
 * FXML Controller class
 *
 * @author Moacir
 */
public class ClientesController implements Initializable {

    @FXML private AnchorPane AnchorPane;
    @FXML private TextField edtCodigo;
    @FXML private TextField edtNome;
    @FXML private TextField edtEndereco;
    @FXML private TextField edtBairro;
    @FXML private TextField edtCidade;
    @FXML private ComboBox cmbUF;
    @FXML private TextField edtCEP;
    @FXML private TextField edtTel;
    @FXML private TableView tblClientes;
    @FXML private Button btnTest;
    
    private SICEV application;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
                    
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
    
    public void setApp (SICEV app) {
        this.application = app;
    }
    
}
