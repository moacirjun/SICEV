/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.model;

import java.time.LocalDate;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Moacir
 */
public class ModelVenda {

    private final IntegerProperty idVenda = new SimpleIntegerProperty();
    private final IntegerProperty idCliente = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> dataVenda = new SimpleObjectProperty<>();
    private final DoubleProperty valorTotal = new SimpleDoubleProperty();
    private final DoubleProperty valor = new SimpleDoubleProperty();
    private final DoubleProperty desconto = new SimpleDoubleProperty();
    private final StringProperty nomeCliente = new SimpleStringProperty();

    public int getIdVenda() {
        return idVenda.get();
    }

    public void setIdVenda(int value) {
        idVenda.set(value);
    }

    public IntegerProperty idVendaProperty() {
        return idVenda;
    }
    
    public int getIdCliente() {
        return idCliente.get();
    }

    public void setIdCliente(int value) {
        idCliente.set(value);
    }

    public IntegerProperty idClienteProperty() {
        return idCliente;
    }
    
    public LocalDate getDataVenda() {
        return dataVenda.get();
    }

    public void setDataVenda(LocalDate value) {
        dataVenda.set(value);
    }

    public ObjectProperty<LocalDate> dataVendaProperty() {
        return dataVenda;
    }
    
    public double getValorTotal() {
        return valorTotal.get();
    }

    public void setValorTotal(double value) {
        valorTotal.set(value);
    }

    public DoubleProperty valorTotalProperty() {
        return valorTotal;
    }
    
    public double getValor() {
        return valor.get();
    }

    public void setValor(double value) {
        valor.set(value);
    }

    public DoubleProperty valorProperty() {
        return valor;
    }
    
    public double getDesconto() {
        return desconto.get();
    }

    public void setDesconto(double value) {
        desconto.set(value);
    }

    public DoubleProperty descontoProperty() {
        return desconto;
    }
    
    public String getNomeCliente() {
        return nomeCliente.get();
    }

    public void setNomeCliente(String value) {
        nomeCliente.set(value);
    }

    public StringProperty nomeClienteProperty() {
        return nomeCliente;
    }
}
