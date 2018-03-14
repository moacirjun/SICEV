/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Moacir
 */
public class ModelUsuario {

    private final IntegerProperty idUsuario = new SimpleIntegerProperty();
    private final StringProperty usuNome = new SimpleStringProperty();
    private final StringProperty usuLogin = new SimpleStringProperty();
    private final StringProperty usuSenha = new SimpleStringProperty();

    public int getId() {
        return idUsuario.get();
    }

    public void setId(int value) {
        idUsuario.set(value);
    }

    public IntegerProperty idProperty() {
        return idUsuario;
    }
    
    
    public String getNome() {
        return usuNome.get();
    }

    public void setNome(String value) {
        usuNome.set(value);
    }

    public StringProperty nomeProperty() {
        return usuNome;
    }
    
    public String getLogin() {
        return usuLogin.get();
    }

    public void setLogin(String value) {
        usuLogin.set(value);
    }

    public StringProperty loginProperty() {
        return usuLogin;
    }

    public String getSenha() {
        return usuSenha.get();
    }

    public void setSenha(String value) {
        usuSenha.set(value);
    }

    public StringProperty senhaProperty() {
        return usuSenha;
    }
    
}
