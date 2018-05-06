/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.controller;

import java.util.ArrayList;
import sicev.model.ModelVendaProduto;
import sicev.model.ProdutosDaVenda;

/**
 *
 * @author Moacir
 */
public class ProdutosDaVendaController {

    static ArrayList<ProdutosDaVenda> getListaProdutosDaVenda(ArrayList<ModelVendaProduto> lista) {
        ArrayList<ProdutosDaVenda> listaProdutos = new ArrayList<>();
        
        lista.forEach((produtoDaLista) -> {
            ProdutosDaVenda novoProduto = new ProdutosDaVenda();
            
            novoProduto.setIdProduto(produtoDaLista.getIdProduto());
            novoProduto.setProNome(produtoDaLista.getProduto().getProNome());
            novoProduto.setQuantidade(produtoDaLista.getProQtde());
            novoProduto.setProValor(produtoDaLista.getProValor());
            novoProduto.setValorTotal(produtoDaLista.getProduto().getProValor()
                    * produtoDaLista.getProQtde());
            
            listaProdutos.add(novoProduto);
        });
        
        return listaProdutos;
    }
    
}
