/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.DAO;

import java.util.ArrayList;
import sicev.connection.ConnectionPostgreSQL;
import sicev.model.ModelClientes;

/**
 *
 * @author Moacir
 */
public class DaoClientes extends ConnectionPostgreSQL{
    
    public int salvarClienteDAO (ModelClientes pModelClientes) {
        try {
            this.conectar();
            return this.insertSQL("INSERT INTO tb_cliente ("
                    + "cli_nome,"
                    + "cli_endereco,"
                    + "cli_bairro,"
                    + "cli_cidade,"
                    + "cli_uf,"
                    + "cli_cep,"
                    + "cli_telefone) VALUES ("
                    + "'" + pModelClientes.getNome()+ "',"
                    + "'" + pModelClientes.getEndereco()+ "',"
                    + "'" + pModelClientes.getBairro()+ "',"
                    + "'" + pModelClientes.getCidade()+ "',"
                    + "'" + pModelClientes.getUf()+ "',"
                    + "'" + pModelClientes.getCep()+ "',"
                    + "'" + pModelClientes.getTelefone()+ "')"
                    + " RETURNING pk_id_cliente; ");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao inserir cliente. MSG:"+e.getMessage());
            return 0;
        } finally {
            this.fecharConexao();
        }
    }
    
    public boolean excluirClienteDao (int idCliente) {
        try {
            this.conectar();
            return this.executarUpdateDeleteSQL("DELETE FROM tb_cliente "
                    + "WHERE pk_id_cliente = '" + idCliente + "'");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao excluir cliente. MSG:"+e.getMessage());
            return false;
        } finally {
            this.fecharConexao();
        }
    }
    
    public boolean atualizarClienteDao (ModelClientes pModelCliente) {
        try {
            this.conectar();
            return this.executarUpdateDeleteSQL(
                    "UPDATE tb_cliente SET "
                    + "cli_nome = '" + pModelCliente.getNome() + "', " 
                    + "cli_endereco = '" + pModelCliente.getEndereco()+ "', " 
                    + "cli_bairro = '" + pModelCliente.getBairro()+ "', " 
                    + "cli_cidade = '" + pModelCliente.getCidade() + "', " 
                    + "cli_uf = '" + pModelCliente.getUf() + "', " 
                    + "cli_cep = '" + pModelCliente.getCep() + "', " 
                    + "cli_telefone = '" + pModelCliente.getTelefone() + "' "
                    + "WHERE pk_id_cliente = '" + pModelCliente.getIdCliente() + "'" 
             );
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao excluir cliente. MSG:"+e.getMessage());
            return false;
        } finally {
            this.fecharConexao();
        }
    }
    
    public ModelClientes getClienteByIdDao (int idCliente) {
        ModelClientes modelCliente = new ModelClientes();
        
        try {
            this.conectar();
            this.executarSQL("SELECT * FROM tb_cliente "
                    + "WHERE pk_id_cliente = '" + idCliente + "'");
            
            while ( this.getResultSet().next() )  {
                modelCliente.setIdCliente(this.getResultSet().getInt("pk_id_cliente"));
                modelCliente.setNome(this.getResultSet().getString("cli_nome"));
                modelCliente.setEndereco(this.getResultSet().getString("cli_endereco"));
                modelCliente.setBairro(this.getResultSet().getString("cli_bairro"));
                modelCliente.setCidade(this.getResultSet().getString("cli_cidade"));
                modelCliente.setUf(this.getResultSet().getString("cli_uf"));
                modelCliente.setCep(this.getResultSet().getString("cli_cep"));
                modelCliente.setTelefone(this.getResultSet().getString("cli_telefone"));
            }
            
        } catch (Exception e)  {
            e.printStackTrace();
            System.out.println("Erro ao carregar cliente pelo ID. MSG: "+e.getMessage());
        } finally {
            this.fecharConexao();
        }
        
        return modelCliente;
    }
    
    public ArrayList<ModelClientes> getAllClientesDao() {
        ArrayList<ModelClientes> listaClientes = new ArrayList<>();
        
        try {
            this.conectar();
            this.executarSQL("SELECT * FROM tb_cliente ORDER BY pk_id_cliente LIMIT 1000");
            
            while ( this.getResultSet().next() ) {
                ModelClientes modelClientes = new ModelClientes();
                modelClientes.setIdCliente(this.getResultSet().getInt("pk_id_cliente"));
                modelClientes.setNome(this.getResultSet().getString("cli_nome"));
                modelClientes.setEndereco(this.getResultSet().getString("cli_endereco"));
                modelClientes.setBairro(this.getResultSet().getString("cli_bairro"));
                modelClientes.setCidade(this.getResultSet().getString("cli_cidade"));
                modelClientes.setUf(this.getResultSet().getString("cli_uf"));
                modelClientes.setCep(this.getResultSet().getString("cli_cep"));
                modelClientes.setTelefone(this.getResultSet().getString("cli_telefone"));
                listaClientes.add(modelClientes);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar todos os produto. MSG: "+e.getMessage());
        } finally {
            this.fecharConexao();
        }
        
        return listaClientes;
    }
}
