/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.controller;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Moacir
 */
public class FXMLDocumentController implements Initializable {
    
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
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
//        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
//        Stage stage = (Stage) btnTest.getScene().getWindow();
//        System.out.println(getClass());
//        System.out.println(getClass().getClassLoader());
//        System.out.println(getClass().getResource("FXMLProdutos.fxml"));
//        
//        Parent root = FXMLLoader.load(getClass().getResource("../view/FXMLProdutos.fxml"));
//        
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}
