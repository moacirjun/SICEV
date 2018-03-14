/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.DAO;

import java.util.ArrayList;
import sicev.connection.ConnectionPostgreSQL;
import sicev.model.ModelUsuario;

/**
 *
 * @author Moacir
 */
public class DaoUsuario extends ConnectionPostgreSQL {
    
    public int salvarUsuarioDao( ModelUsuario pModelUsuario ) {
        try {
            this.conectar();
            return this.insertSQL("INSERT INTO tb_usuario ("
                    + "usu_nome,"
                    + "usu_login,"
                    + "usu_senha) VALUES ("
                    + "'" + pModelUsuario.getNome()+ "',"
                    + "'" + pModelUsuario.getLogin() + "',"
                    + "'" + pModelUsuario.getSenha() + "') "
                    + " RETURNING pk_id_usuario; ");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao iserir usuario. MSG:"+e.getMessage());
            return 0;
        } finally {
            this.fecharConexao();
        }
    }
    
    public boolean excluirUsuarioDao( int pUsuarioId ) {
        try {
            
            this.conectar();
            return this.executarUpdateDeleteSQL("DELETE FROM tb_usuario WHERE "
                    + "pk_id_usuario = '" + pUsuarioId + "'");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao excluir usuario. MSG:" + e.getMessage());                  
            return false;
        } finally {
            this.fecharConexao();
        }
    }
    
    public boolean atualizarUsuarioDao( ModelUsuario pModelUsuario ) {
        try {
            this.conectar();
            return this.executarUpdateDeleteSQL("UPDATE tb_usuario SET "
                    + "usu_nome = '" + pModelUsuario.getNome() + "', "
                    + "usu_login = '" + pModelUsuario.getLogin() + "', "
                    + "usu_senha = '" + pModelUsuario.getSenha() + "' "
                    + "WHERE pk_id_usuario = '" + pModelUsuario.getId() + "'");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar usuario. MSG:"+e.getMessage());
            return false;
        } finally {
            this.fecharConexao();
        }
    }
    
    public ModelUsuario getUsuarioByIdDao( int pUsuarioId ) {
        ModelUsuario modelUsuario = new ModelUsuario();
        
        try {
            this.conectar();
            this.executarSQL("SELECT * FROM tb_usuario "
                    + "WHERE pk_id_usuario = '" + pUsuarioId + "'");
            
            while ( this.getResultSet().next() ) {
                modelUsuario.setId(this.getResultSet().getInt("pk_id_usuario"));
                modelUsuario.setNome(this.getResultSet().getString("usu_nome"));
                modelUsuario.setLogin(this.getResultSet().getString("usu_login"));
                modelUsuario.setSenha(this.getResultSet().getString("usu_senha"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar usuario pelo ID. MSG: "+e.getMessage());
        } finally {
            this.fecharConexao();
        }
        
        return modelUsuario;
    }
    
    public ArrayList<ModelUsuario> getAllUsuariosDao() {
        ArrayList<ModelUsuario> listaUsuarios = new ArrayList<>();
        
        try {
            this.conectar();
            this.executarSQL("SELECT * FROM tb_usuario ORDER BY pk_id_usuario LIMIT 1000");
            
            while ( this.getResultSet().next() ) {
                ModelUsuario modelUsuario = new ModelUsuario();
                modelUsuario.setId(this.getResultSet().getInt("pk_id_usuario"));
                modelUsuario.setNome(this.getResultSet().getString("usu_nome"));
                modelUsuario.setLogin(this.getResultSet().getString("usu_login"));
                modelUsuario.setSenha(this.getResultSet().getString("usu_senha"));
                listaUsuarios.add(modelUsuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar todos os usuarios. MSG: "+e.getMessage());
        } finally {
            this.fecharConexao();
        }
        
        return listaUsuarios;
    }
    
    public static boolean autenticaUsuario (String userName, String userPass) {
        
        ConnectionPostgreSQL tempCon = new ConnectionPostgreSQL();
        
        try {
            tempCon.conectar();
            tempCon.executarSQL("SELECT pk_id_usuario FROM tb_usuario "
                    + "WHERE usu_login = '" + userName + "'" + " AND "
                            + "usu_senha = '" + userPass + "'");
            
            return tempCon.getResultSet().next();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao autenticar usuario. MSG: "+e.getMessage());
            return false;
        } finally {
            tempCon.fecharConexao();
        }
    }

}
