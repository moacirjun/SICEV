/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.DAO;

import java.util.ArrayList;
import sicev.connection.ConnectionPostgreSQL;
import sicev.model.ModelVendaProduto;

/**
 *
 * @author Moacir
 */
public class DaoVendaProduto extends ConnectionPostgreSQL{
    
    public int SalvarVendaProdutoDao (ModelVendaProduto pModelVendaProduto) {
        try {
            this.conectar();
            return this.insertSQL("INSERT INTO tb_venda_produto ("
                    + "fk_id_venda,"
                    + "fk_id_produto,"
                    + "vend_pro_valor,"
                    + "vend_pro_qtd) VALUES ("
                    + "'" + pModelVendaProduto.getIdVenda()+ "',"
                    + "'" + pModelVendaProduto.getIdProduto()+ "',"
                    + "'" + pModelVendaProduto.getProValor()+ "',"
                    + "'" + pModelVendaProduto.getProQtde()+ "')"
                    + " RETURNING pk_id_venda_produto; ");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao inserir produto da venda. MSG:"+e.getMessage());
            return 0;
        } finally {
            this.fecharConexao();
        }
    }
    
    public boolean excluirVendaProdutoDao (int idVendaProd) {
        try {
            this.conectar();
            return this.executarUpdateDeleteSQL("DELETE FROM tb_venda_produto "
                    + "WHERE pk_id_venda_produto = '" + idVendaProd + "'");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao excluir produto da venda. MSG:"+e.getMessage());
            return false;
        } finally {
            this.fecharConexao();
        }
    }
    
    public boolean atualizarVendaProdutoDao (ModelVendaProduto pModelVendaProduto) {
        try {
            this.conectar();
            return this.executarUpdateDeleteSQL(
                    "UPDATE tb_venda SET "
                    + "fk_id_venda = '" + pModelVendaProduto.getIdVenda() + "', "
                    + "fk_id_produto = '" + pModelVendaProduto.getIdProduto() + "', "
                    + "vend_pro_valor = '" + pModelVendaProduto.getProValor() + "', "
                    + "vend_pro_qtd = '" + pModelVendaProduto.getProQtde() + "' "
                    + "WHERE pk_id_venda_produto = '" + pModelVendaProduto.getIdVendaProduto()+ "'" 
             );
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar produto da venda. MSG:"+e.getMessage());
            return false;
        } finally {
            this.fecharConexao();
        }
    }
    
    public ModelVendaProduto getVendaProdutoById (int idVendaProduto) {
        ModelVendaProduto modelVenda = new ModelVendaProduto();
        
        try {
            this.conectar();
            this.executarSQL("SELECT * FROM tb_venda_produto "
                    + "WHERE pk_id_venda_produto = '" + idVendaProduto + "'");
            
            while ( this.getResultSet().next() )  {
                modelVenda.setIdVendaProduto(this.getResultSet().getInt("pk_id_venda_produto"));
                modelVenda.setIdVenda(this.getResultSet().getInt("pk_id_venda"));
                modelVenda.setIdProduto(this.getResultSet().getInt("pk_id_produto"));
                modelVenda.setProValor(this.getResultSet().getDouble("vend_pro_valor"));
                modelVenda.setProQtde(this.getResultSet().getDouble("vend_pro_qtd"));
            }
            
        } catch (Exception e)  {
            e.printStackTrace();
            System.out.println("Erro ao carregar venda pelo ID. MSG: "+e.getMessage());
        } finally {
            this.fecharConexao();
        }
        
        return modelVenda;
    }
    
    public ArrayList<ModelVendaProduto> getAllVendasProdutosDao() {
        ArrayList<ModelVendaProduto> listaVendaProds = new ArrayList<>();
        
        try {
            this.conectar();
            this.executarSQL("SELECT * FROM tb_venda_produto ORDER BY pk_id_venda_produto LIMIT 1000");
            
            while ( this.getResultSet().next() ) {
                ModelVendaProduto modelVendaProd = new ModelVendaProduto();
                modelVendaProd.setIdVendaProduto(this.getResultSet().getInt("pk_id_venda_produto"));
                modelVendaProd.setIdVenda(this.getResultSet().getInt("pk_id_venda"));
                modelVendaProd.setIdProduto(this.getResultSet().getInt("pk_id_produto"));
                modelVendaProd.setProValor(this.getResultSet().getDouble("vend_pro_valor"));
                modelVendaProd.setProQtde(this.getResultSet().getDouble("vend_pro_qtd"));
                listaVendaProds.add(modelVendaProd);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar todos os produtos das vendas. MSG: "+e.getMessage());
        } finally {
            this.fecharConexao();
        }
        
        return listaVendaProds;
    }
    
    public ArrayList<ModelVendaProduto> getAllProdutosByIdVendaDao(int idVenda) {
        ArrayList<ModelVendaProduto> listaVendaProds = new ArrayList<>();
        
        try {
            this.conectar();
            this.executarSQL("SELECT * FROM tb_venda_produto "
                    + "WHERE fk_id_venda = " + "'" + idVenda + "' "
                    + "ORDER BY pk_id_venda_produto");
            
            while ( this.getResultSet().next() ) {
                ModelVendaProduto modelVendaProd = new ModelVendaProduto();
                modelVendaProd.setIdVendaProduto(this.getResultSet().getInt("pk_id_venda_produto"));
                modelVendaProd.setIdVenda(this.getResultSet().getInt("pk_id_venda"));
                modelVendaProd.setIdProduto(this.getResultSet().getInt("pk_id_produto"));
                modelVendaProd.setProValor(this.getResultSet().getDouble("vend_pro_valor"));
                modelVendaProd.setProQtde(this.getResultSet().getDouble("vend_pro_qtd"));
                listaVendaProds.add(modelVendaProd);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar todos os produtos da venda. MSG: "+e.getMessage());
        } finally {
            this.fecharConexao();
        }
        
        return listaVendaProds;
    }
    
    public static boolean salvaProdutosDaVendaDao(ArrayList<ModelVendaProduto> listaProdutos) {
        ConnectionPostgreSQL tempCon = new ConnectionPostgreSQL();
        tempCon.conectar();
        
        try {
            listaProdutos.forEach((produto) -> {
                
                tempCon.insertSQL("INSERT INTO tb_venda_produto ("
                        + "fk_id_venda,"
                        + "fk_id_produto,"
                        + "vend_pro_valor,"
                        + "vend_pro_qtd) VALUES ("
                        + "'" + produto.getIdVenda()+ "',"
                        + "'" + produto.getIdProduto()+ "',"
                        + "'" + produto.getProValor()+ "',"
                        + "'" + produto.getProQtde()+ "')"
                        + " RETURNING pk_id_venda_produto; ");
            });
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao inserir produto da venda. MSG:"+e.getMessage());
            return false;
        } finally {
            tempCon.fecharConexao();
        }        
    }
}
