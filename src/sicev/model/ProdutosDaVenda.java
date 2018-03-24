/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author Moacir
 */
public class ProdutosDaVenda extends ModelProdutos {

    private final DoubleProperty quantidade = new SimpleDoubleProperty();
    private final DoubleProperty valorTotal = new SimpleDoubleProperty();

    public ProdutosDaVenda(ModelProdutos produto) {
        super.setIdProduto(produto.getIdProduto());
        super.setProNome(produto.getProNome());
        super.setProValor(produto.getProValor());
        super.setProEstoque(produto.getProEstoque());
        super.setProEstoqueMin(produto.getProEstoqueMin());
    }
    
    public double getQuantidade() {
        return quantidade.get();
    }

    public void setQuantidade(double value) {
        quantidade.set(value);
    }

    public DoubleProperty quantidadeProperty() {
        return quantidade;
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
}
