/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.controller;

import java.util.ArrayList;
import sicev.DAO.DaoVendaProduto;
import sicev.model.ModelVendaProduto;

/**
 *
 * @author Moacir
 */
public class VendaProdutoController {
    
    private final DaoVendaProduto daoVendaProduto = new DaoVendaProduto();
    
    public int SalvarVendaProdutoDao (ModelVendaProduto pModelVendaProduto) {
        return daoVendaProduto.SalvarVendaProdutoDao(pModelVendaProduto);
    }
    
    public boolean excluirVendaProdutoDao (int idVendaProd) {
        return daoVendaProduto.excluirVendaProdutoDao(idVendaProd);
    }
    
    public boolean atualizarVendaProdutoDao (ModelVendaProduto pModelVendaProduto) {
        return daoVendaProduto.atualizarVendaProdutoDao(pModelVendaProduto);
    }
    
    public ModelVendaProduto getVendaProdutoById (int idVendaProduto) {
        return daoVendaProduto.getVendaProdutoById(idVendaProduto);
    }
    
    public ArrayList<ModelVendaProduto> getAllVendasProdutosDao() {
        return daoVendaProduto.getAllVendasProdutosDao();
    }
    
    public ArrayList<ModelVendaProduto> getAllProdutosByIdVendaDao(int idVenda) {
        return daoVendaProduto.getAllProdutosByIdVendaDao(idVenda);
    }
    
    public ArrayList<ModelVendaProduto> getAllProdutosByIdVendaDao(ModelVendaProduto venda) {
        return daoVendaProduto.getAllProdutosByIdVendaDao(venda);
    }
    
    public boolean salvaProdutosDaVendaDao(ArrayList<ModelVendaProduto> listaProdutos) {
        return daoVendaProduto.salvaProdutosDaVendaDao(listaProdutos);
    }
}
