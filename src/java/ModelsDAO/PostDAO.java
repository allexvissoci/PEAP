/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.Post;
import Models.Usuario;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author allex
 */
public class PostDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelect = "select * from Post order by id desc";
    private String stmtSelectById = "select * From Post where id = ?;";
    private String stmtInsert = "insert into Post (titulo, texto, dataCriacao, dataPublicacao, usuario) values (?, ?, now(), now(), ?);";
    private String stmtUpdate = "update Post set titulo = ?, texto = ? where id = ?;";
//    private String stmtUpdate = "update Post set titulo = ?, texto = ?, dataCriacao = ?, dataPublicacao = ?, usuario = ? where id = ?;";
    private String stmtDelete = "DELETE FROM Post WhERE id = ?;";
    
    
    public List<Post> getLista() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelect);
            rs = stmt.executeQuery();
            List<Post> lstpost = new ArrayList();
            
            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                post.setTitulo(rs.getString("titulo"));
                post.setTexto(rs.getString("texto"));
                
                java.sql.Timestamp dataCriacaoStamp = rs.getTimestamp("dataCriacao");
                java.sql.Date dataCriacao = new Date( dataCriacaoStamp.getTime());
                post.setDataCriacao(dataCriacao);
                
                java.sql.Timestamp dataPublicacaoStamp = rs.getTimestamp("dataPublicacao");
                java.sql.Date dataPublicacao = new Date( dataPublicacaoStamp.getTime());
                post.setDataPublicacao(dataPublicacao);
                
                Usuario usuario = new Usuario();
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                usuario = usuarioDAO.getById(rs.getInt("usuario"));
                post.setUsuario(usuario);
                
                lstpost.add(post);
            }
            return lstpost;

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
    
    public void delete(int id) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(stmtDelete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Post getById(int id) throws ClassNotFoundException {
        Post post = new Post();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                post.setId(rs.getInt("id"));
                post.setTitulo(rs.getString("titulo"));
                post.setTexto(rs.getString("texto"));
                Usuario usuario = new Usuario();
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                usuario = usuarioDAO.getById(rs.getInt("usuario"));
                post.setUsuario(usuario);
            }
            return post;
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
    
    public void insert(Post post){
        PreparedStatement stmt = null;
        int idObjeto = 0;
        try {
            stmt = con.prepareStatement(stmtInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, post.getTitulo());
            stmt.setString(2, post.getTexto());
            stmt.setInt(3, post.getUsuario().getId());
            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
            }

            post.setId(idObjeto);

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
    
    public void update(Post post) {
        PreparedStatement stmt = null;
        try {
            if(post.getId() > 0 ){
                stmt = con.prepareStatement(stmtUpdate);
                stmt.setString(1, post.getTitulo());
                stmt.setString(2, post.getTexto());
                stmt.setInt(3, post.getId());
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
}
