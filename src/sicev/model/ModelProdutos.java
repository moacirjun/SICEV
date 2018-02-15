package sicev.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Moacir
 */
public class ModelProdutos {
    private final IntegerProperty idProduto = new SimpleIntegerProperty();
    private final StringProperty proNome = new SimpleStringProperty();
    private final DoubleProperty proValor = new SimpleDoubleProperty();
    private final IntegerProperty proEstoque = new SimpleIntegerProperty();
    private final IntegerProperty proEstoqueMin = new SimpleIntegerProperty();
    
    public int getIdProduto() {
        return idProduto.get();
    }

    public void setIdProduto(int value) {
        idProduto.set(value);
    }

    public IntegerProperty idProdutoProperty() {
        return idProduto;
    }

    public String getProNome() {
        return proNome.get();
    }

    public void setProNome(String value) {
        proNome.set(value);
    }

    public StringProperty proNomeProperty() {
        return proNome;
    }
    
    public Double getProValor() {
        return proValor.get();
    }

    public void setProValor(Double value) {
        proValor.set(value);
    }

    public DoubleProperty proValorProperty() {
        return proValor;
    }
    
    public int getProEstoque() {
        return proEstoque.get();
    }

    public void setProEstoque(int value) {
        proEstoque.set(value);
    }

    public IntegerProperty proEstoqueProperty() {
        return proEstoque;
    }

    public int getProEstoqueMin() {
        return proEstoqueMin.get();
    }

    public void setProEstoqueMin(int value) {
        proEstoqueMin.set(value);
    }
    
    public IntegerProperty proEstoqueMinProperty() {
        return proEstoqueMin;
    }
}
