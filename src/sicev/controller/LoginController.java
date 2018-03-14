/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sicev.view.SICEV;

/**
 * FXML Controller class
 *
 * @author Moacir
 */
public class LoginController implements Initializable {

    @FXML private TextField edtUserName;
    @FXML private TextField edtUserPass;
    @FXML private Button btnLogin;
    @FXML private Label lblErroMessage;
    
    
    private SICEV application;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        edtUserName.setText("MoacirjuN");
        edtUserPass.setText("123");
        btnLogin.requestFocus();
    }    
    
    public void setApp(SICEV application) {
        this.application = application;
    }
    
    public void processLogin(ActionEvent event) {
        if ( application == null ) {
            lblErroMessage.setText("Olá "+edtUserName.getText());
        }
        else {
            if ( !application.UserLogging(edtUserName.getText(), edtUserPass.getText()) ) {
                lblErroMessage.setText("Usuário e/ou senha inválidos!");
            }
        }
    }
    
}
