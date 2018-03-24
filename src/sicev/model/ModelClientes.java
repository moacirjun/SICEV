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
public class ModelClientes {

    private final IntegerProperty idCliente = new SimpleIntegerProperty();
    private final StringProperty cliNome = new SimpleStringProperty();
    private final StringProperty cliEndereco = new SimpleStringProperty();
    private final StringProperty cliBairro = new SimpleStringProperty();
    private final StringProperty cliCidade = new SimpleStringProperty();
    private final StringProperty cliUf = new SimpleStringProperty();
    private final StringProperty cliCep = new SimpleStringProperty();
    private final StringProperty cliTel = new SimpleStringProperty();

    /**
     * 
     * @return 
     */
    public int getIdCliente() {
        return idCliente.get();
    }

    /**
     * 
     * @param value 
     */
    public void setIdCliente(int value) {
        idCliente.set(value);
    }

    /**
     * 
     * @return 
     */
    public IntegerProperty idClienteProperty() {
        return idCliente;
    }
    
    /**
     * 
     * @return 
     */
    public String getNome() {
        return cliNome.get();
    }

    /**
     * 
     * @param value 
     */
    public void setNome(String value) {
        cliNome.set(value);
    }

    /**
     * 
     * @return 
     */
    public StringProperty nomeProperty() {
        return cliNome;
    }
    
    /**
     * 
     * @return 
     */
    public String getEndereco() {
        return cliEndereco.get();
    }

    /**
     * 
     * @param value 
     */
    public void setEndereco(String value) {
        cliEndereco.set(value);
    }

    /**
     * 
     * @return 
     */
    public StringProperty enderecoProperty() {
        return cliEndereco;
    }
    
    /**
     * 
     * @return 
     */
    public String getBairro() {
        return cliBairro.get();
    }

    /**
     * 
     * @param value 
     */
    public void setBairro(String value) {
        cliBairro.set(value);
    }

    /**
     * 
     * @return 
     */
    public StringProperty bairroProperty() {
        return cliBairro;
    }
    
    /**
     * 
     * @return 
     */
    public String getCidade() {
        return cliCidade.get();
    }

    /**
     * 
     * @param value 
     */
    public void setCidade(String value) {
        cliCidade.set(value);
    }

    /**
     * 
     * @return 
     */
    public StringProperty cidadeProperty() {
        return cliCidade;
    }
    
    /**
     * 
     * @return 
     */
    public String getUf() {
        return cliUf.get();
    }

    /**
     * 
     * @param value 
     */
    public void setUf(String value) {
        cliUf.set(value);
    }

    /**
     * 
     * @return 
     */
    public StringProperty ufProperty() {
        return cliUf;
    }
    
    /**
     * 
     * @return 
     */
    public String getCep() {
        return cliCep.get();
    }

    /**
     * 
     * @param value 
     */
    public void setCep(String value) {
        cliCep.set(value);
    }

    /**
     * 
     * @return 
     */
    public StringProperty cepProperty() {
        return cliCep;
    }
    
    /**
     * 
     * @return 
     */
    public String getTelefone() {
        return cliTel.get();
    }

    /**
     * 
     * @param value 
     */
    public void setTelefone(String value) {
        cliTel.set(value);
    }

    /**
     * 
     * @return 
     */
    public StringProperty telefoneProperty() {
        return cliTel;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.getIdCliente()) + " - " + this.getNome();
    }
}
