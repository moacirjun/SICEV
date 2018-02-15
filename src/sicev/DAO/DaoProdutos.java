/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.DAO;

import java.util.ArrayList;
import sicev.connection.ConnectionPostgreSQL;
import sicev.model.ModelProdutos;

/**
 *
 * @author Moacir
 */
public class DaoProdutos extends ConnectionPostgreSQL{
    
    /**
     * salva um novo produto
     * @param pModelProduto
     * @return 
     */
    public int salvarProdutosDAO(ModelProdutos pModelProduto) {
        try {
            this.conectar();
            return this.insertSQL("INSERT INTO tb_produto ("
                    + "pro_nome,"
                    + "pro_valor,"
                    + "pro_estoque,"
                    + "pro_estoque_min) VALUES ("
                    + "'" + pModelProduto.getProNome() + "',"
                    + "'" + pModelProduto.getProValor() + "',"
                    + "'" + pModelProduto.getProEstoque() + "',"
                    + "'" + pModelProduto.getProEstoqueMin() + "') "
                    + " RETURNING pk_id_produto; ");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao iserir produto. MSG:"+e.getMessage());
            return 0;
        } finally {
            this.fecharConexao();
        }
    }
    
    /**
     * Exclue um produto
     * @param pIdProduto
     * @return boolean com o sttatus da exlusão
     */
    public boolean excluirProdutoDAO(int pIdProduto) {
        try {
            
            this.conectar();
            return this.executarUpdateDeleteSQL("DELETE FROM tb_produto WHERE "
                    + "pk_id_produto = '" + pIdProduto + "'");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao excluir produto. MSG:" + e.getMessage());                  
            return false;
        } finally {
            this.fecharConexao();
        }
    }
    
    /**
     * atualiza o produto com o id passado
     * @param pModelProduto
     * @return boolean com o status da atualização
     */
    public boolean atualizarProdutoDAO(ModelProdutos pModelProduto) {
        try {
            this.conectar();
            return this.executarUpdateDeleteSQL("UPDATE tb_produto SET "
                    + "pro_nome = '" + pModelProduto.getProNome() + "', "
                    + "pro_valor = '" + pModelProduto.getProValor() + "', "
                    + "pro_estoque = '" + pModelProduto.getProEstoque() + "', "
                    + "pro_estoque_min = '" + pModelProduto.getProEstoqueMin() + "' "
                    + "WHERE pk_id_produto = '" + pModelProduto.getIdProduto() + "'");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar produto. MSG:"+e.getMessage());
            return false;
        } finally {
            this.fecharConexao();
        }
    }
    
    public ModelProdutos getProdutoByIdDao(int pIdProduto) {
        ModelProdutos modelProduto = new ModelProdutos();
        
        try {
            this.conectar();
            this.executarSQL("SELECT * FROM tb_produto "
                    + "WHERE pk_id_produto = '" + pIdProduto + "'");
            
            while ( this.getResultSet().next() ) {
                modelProduto.setIdProduto(this.getResultSet().getInt("pk_id_produto"));
                modelProduto.setProNome(this.getResultSet().getString("pro_nome"));
                modelProduto.setProValor(this.getResultSet().getDouble("pro_valor"));
                modelProduto.setProEstoque(this.getResultSet().getInt("pro_estoque"));
                modelProduto.setProEstoqueMin(this.getResultSet().getInt("pro_estoque_min"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar produto pelo ID. MSG: "+e.getMessage());
        } finally {
            this.fecharConexao();
        }
        
        return modelProduto;
    }
    
    public ArrayList<ModelProdutos> getAllProdutosDao() {
        ArrayList<ModelProdutos> listaProdutos = new ArrayList<>();
        
        try {
            this.conectar();
            this.executarSQL("SELECT * FROM tb_produto ORDER BY pk_id_produto LIMIT 1000");
            
            while ( this.getResultSet().next() ) {
                ModelProdutos modelProduto = new ModelProdutos();
                modelProduto.setIdProduto(this.getResultSet().getInt("pk_id_produto"));
                modelProduto.setProNome(this.getResultSet().getString("pro_nome"));
                modelProduto.setProValor(this.getResultSet().getDouble("pro_valor"));
                modelProduto.setProEstoque(this.getResultSet().getInt("pro_estoque"));
                modelProduto.setProEstoqueMin(this.getResultSet().getInt("pro_estoque_min"));
                listaProdutos.add(modelProduto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar todos os produto. MSG: "+e.getMessage());
        } finally {
            this.fecharConexao();
        }
        
        return listaProdutos;
    }
}
