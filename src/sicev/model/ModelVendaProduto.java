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

/**
 *
 * @author Moacir
 */
public class ModelVendaProduto {

    private final IntegerProperty idVendaProduto = new SimpleIntegerProperty();
    private final IntegerProperty idVenda = new SimpleIntegerProperty();
    private final IntegerProperty idProduto = new SimpleIntegerProperty();
    private final DoubleProperty proValor = new SimpleDoubleProperty();
    private final DoubleProperty proQtde = new SimpleDoubleProperty();

    public int getIdVendaProduto() {
        return idVendaProduto.get();
    }

    public void setIdVendaProduto(int value) {
        idVendaProduto.set(value);
    }

    public IntegerProperty idVendaProdutoProperty() {
        return idVendaProduto;
    }
    
    public int getIdProduto() {
        return idProduto.get();
    }

    public void setIdProduto(int value) {
        idProduto.set(value);
    }

    public IntegerProperty idProdutoProperty() {
        return idProduto;
    }
    
    public int getIdVenda() {
        return idVenda.get();
    }

    public void setIdVenda(int value) {
        idVenda.set(value);
    }

    public IntegerProperty idVendaProperty() {
        return idVenda;
    }
    
    public double getProValor() {
        return proValor.get();
    }

    public void setProValor(double value) {
        proValor.set(value);
    }

    public DoubleProperty proValorProperty() {
        return proValor;
    }
    
    public double getProQtde() {
        return proQtde.get();
    }

    public void setProQtde(double value) {
        proQtde.set(value);
    }

    public DoubleProperty proQtdeProperty() {
        return proQtde;
    }
}
