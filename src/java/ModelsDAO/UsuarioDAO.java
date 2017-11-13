/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.Curso;
import Models.TipoUsuario;
import Models.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author allex
 */
public class UsuarioDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtCheckLogin = "select * from Usuario where id = ? and senha= MD5(?);";
    private String stmtSelect = "select * from Usuario ";
    private String stmtInsert = "INSERT INTO Usuario (nome, senha, status, tipoUsuario) VALUES (?, MD5(?), ?, ?)";
    private String stmtSelectById = "select * from Usuario where id = ?";
    private String stmtUpdate = "update Usuario set nome = ?, status = ?, tipoUsuario = ? where id = ?;";
    private String stmtResetPassword = "update Usuario set senha = MD5(?) where id = ?;";    

    
    public List<Usuario> getListaToSelect() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            List<Usuario> lstusuario = new ArrayList();
            stmt = con.prepareStatement(stmtSelect);
            rs = stmt.executeQuery();
            Usuario usuario = new Usuario();
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            while(rs.next()){
                usuario = usuarioDAO.getById(rs.getInt("id"));
                lstusuario.add(usuario);
            }
            return lstusuario;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (Exception ex) {
                System.out.println("Erro ao fechar result set. Ex=" + ex.getMessage());
            };
            try {
                stmt.close();
            } catch (Exception ex) {
                System.out.println("Erro ao fechar stmt. Ex=" + ex.getMessage());
            };
        }
    }
    
    public void resetPassword(Usuario usuario) {
        PreparedStatement stmt = null;
        try {
            if(usuario.getId() > 0 ){
                stmt = con.prepareStatement(stmtResetPassword);
                stmt.setString(1, usuario.getSenha());
                stmt.setInt(2, usuario.getId());
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                stmt.close();
            } catch (Exception ex) {
                System.out.println("Erro ao fechar stmt. Ex=" + ex.getMessage());
            };
            
        }
    }
    
    public void update(Usuario usuario) {
        PreparedStatement stmt = null;
        try {
            if(usuario.getId() > 0 ){
                stmt = con.prepareStatement(stmtUpdate);
                stmt.setString(1, usuario.getNome());
                stmt.setBoolean(2, usuario.isStatus());
                stmt.setInt(3, usuario.getTipoUsuario().getId());
                stmt.setInt(4, usuario.getId());
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                stmt.close();
            } catch (Exception ex) {
                System.out.println("Erro ao fechar stmt. Ex=" + ex.getMessage());
            };
            
        }
    }
    
    
    public Usuario verificaLogin(String usu, String senha) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Usuario usuario = new Usuario();
        PreparedStatement stmt = null;

        ResultSet rs = null;
        try {

            stmt = con.prepareStatement(stmtCheckLogin, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, usu);
            stmt.setString(2,senha);
            rs = stmt.executeQuery();
            while (rs.next()) {
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setStatus(rs.getBoolean("status"));
                int tipoUsuarioId = rs.getInt("tipoUsuario");
                TipoUsuarioDAO tipousuDAO = new TipoUsuarioDAO();
                TipoUsuario tipousuario = tipousuDAO.getById(tipoUsuarioId);
                usuario.setTipoUsuario(tipousuario);
            }

            return usuario;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                stmt.close();
            } catch (Exception ex) {
                System.out.println("Erro ao fechar stmt. Ex=" + ex.getMessage());
            };
            
        }
    }
    
    
    public List<Usuario> getLista() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelect);
            rs = stmt.executeQuery();
            List<Usuario> lstusuario = new ArrayList();

            while (rs.next()) {
                // criando o objeto Usuario
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                TipoUsuario tipousu = new TipoUsuario();
                TipoUsuarioDAO tipousuDAO = new TipoUsuarioDAO();
                tipousu = tipousuDAO.getById(rs.getInt("tipoUsuario"));
                usuario.setTipoUsuario(tipousu);
                usuario.setStatus(rs.getBoolean("status")); 

                lstusuario.add(usuario);
            }
            return lstusuario;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (Exception ex) {
                System.out.println("Erro ao fechar result set. Ex=" + ex.getMessage());
            };
            try {
                stmt.close();
            } catch (Exception ex) {
                System.out.println("Erro ao fechar stmt. Ex=" + ex.getMessage());
            };
            
        }
    }
    
    public Usuario getById(int id) throws ClassNotFoundException {
        Usuario usuario = new Usuario();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setSenha(rs.getString("senha"));
                TipoUsuario tipousu = new TipoUsuario();
                TipoUsuarioDAO tipousuDAO = new TipoUsuarioDAO();
                tipousu = tipousuDAO.getById(rs.getInt("tipoUsuario"));
                usuario.setTipoUsuario(tipousu);
                usuario.setStatus(rs.getBoolean("status"));
                
            }
            return usuario;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                stmt.close();
            } catch (Exception ex) {
                System.out.println("Erro ao fechar stmt. Ex=" + ex.getMessage());
            };
            
        }

    }
    
    public void insert(Usuario usuario){
        PreparedStatement stmt = null;
        int idObjeto = 0;
        try {
            stmt = con.prepareStatement(stmtInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getSenha());
            stmt.setBoolean(3, usuario.isStatus());
            stmt.setInt(4, usuario.getTipoUsuario().getId());

            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
            }

            usuario.setId(idObjeto);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                stmt.close();
            } catch (Exception ex) {
                System.out.println("Erro ao fechar stmt. Ex=" + ex.getMessage());
            };
            
        }
    }    
}
