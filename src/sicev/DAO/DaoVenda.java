/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.DAO;

import java.util.ArrayList;
import sicev.connection.ConnectionPostgreSQL;
import sicev.model.ModelVenda;

/**
 *
 * @author Moacir
 */
public class DaoVenda extends ConnectionPostgreSQL{
    
    public int SalvarVendaDao (ModelVenda pModelVenda) {
        try {
            this.conectar();
            return this.insertSQL("INSERT INTO tb_venda ("
                    + "fk_id_cliente,"
                    + "vend_data_venda,"
                    + "vend_valor_total,"
                    + "vend_valor,"
                    + "vend_desconto) VALUES ("
                    + "'" + pModelVenda.getIdCliente()+ "',"
                    + "'" + pModelVenda.getDataVenda()+ "',"
                    + "'" + pModelVenda.getValorTotal()+ "',"
                    + "'" + pModelVenda.getValor()+ "',"
                    + "'" + pModelVenda.getDesconto()+ "')"
                    + " RETURNING pk_id_venda; ");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao inserir venda. MSG:"+e.getMessage());
            return 0;
        } finally {
            this.fecharConexao();
        }
    }
    
    public boolean excluirVendaDao (int idVenda) {
        try {
            this.conectar();
            return this.executarUpdateDeleteSQL("DELETE FROM tb_venda "
                    + "WHERE pk_id_venda = '" + idVenda + "'");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao excluir venda. MSG:"+e.getMessage());
            return false;
        } finally {
            this.fecharConexao();
        }
    }
    
    public boolean atualizarVendaDao (ModelVenda pModelVenda) {
        try {
            this.conectar();
            return this.executarUpdateDeleteSQL(
                    "UPDATE tb_venda SET "
                    + "fk_id_cliente = '" + pModelVenda.getIdCliente() + "', "
                    + "vend_data_venda = '" + pModelVenda.getDataVenda() + "', "
                    + "vend_valor_total = '" + pModelVenda.getValorTotal() + "', "
                    + "vend_valor = '" + pModelVenda.getValor() + "', "
                    + "vend_desconto = '" + pModelVenda.getDesconto() + "' "
                    + "WHERE pk_id_venda = '" + pModelVenda.getIdVenda() + "'" 
             );
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar venda. MSG:"+e.getMessage());
            return false;
        } finally {
            this.fecharConexao();
        }
    }
    
    public ModelVenda getVendaById (int idVenda) {
        ModelVenda modelVenda = new ModelVenda();
        
        try {
            this.conectar();
            this.executarSQL("SELECT * FROM tb_venda "
                    + "WHERE pk_id_venda = '" + idVenda + "'");
            
            while ( this.getResultSet().next() )  {
                modelVenda.setIdVenda(this.getResultSet().getInt("pk_id_venda"));
                modelVenda.setIdCliente(this.getResultSet().getInt("fk_id_cliente"));
                modelVenda.setDataVenda(this.getResultSet().getDate("vend_data_venda").toLocalDate());
                modelVenda.setValorTotal(this.getResultSet().getDouble("vend_valor_total"));
                modelVenda.setValor(this.getResultSet().getDouble("vend_valor"));
                modelVenda.setDesconto(this.getResultSet().getDouble("vend_desconto"));
            }
            
        } catch (Exception e)  {
            e.printStackTrace();
            System.out.println("Erro ao carregar venda pelo ID. MSG: "+e.getMessage());
        } finally {
            this.fecharConexao();
        }
        
        return modelVenda;
    }
    
    public ArrayList<ModelVenda> getAllVendasDao() {
        ArrayList<ModelVenda> listaVendas = new ArrayList<>();
        
        try {
            this.conectar();
            this.executarSQL("SELECT * FROM tb_venda ORDER BY pk_id_venda LIMIT 1000");
            
            while ( this.getResultSet().next() ) {
                ModelVenda modelVenda = new ModelVenda();
                modelVenda.setIdVenda(this.getResultSet().getInt("pk_id_vendae"));
                modelVenda.setIdCliente(this.getResultSet().getInt("fk_id_cliente"));
                modelVenda.setDataVenda(this.getResultSet().getDate("vend_data_venda").toLocalDate());
                modelVenda.setValorTotal(this.getResultSet().getDouble("vend_valor_total"));
                modelVenda.setValor(this.getResultSet().getDouble("vend_valor"));
                modelVenda.setDesconto(this.getResultSet().getDouble("vend_desconto"));
                listaVendas.add(modelVenda);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar todas as vendas. MSG: "+e.getMessage());
        } finally {
            this.fecharConexao();
        }
        
        return listaVendas;
    }
}
