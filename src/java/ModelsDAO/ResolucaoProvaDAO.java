/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.Prova;
import Models.ResolucaoProva;
import Models.Usuario;
import java.sql.Connection;
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
public class ResolucaoProvaDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelectById = "select * From ResolucaoProva where id = ?;";
    private String stmtInsert = "insert into ResolucaoProva (prova, notaFinal, usuario) values (?, ?, ?);";
    private String stmtUpdate = "update ResolucaoProva set prova = ?, notaFinal = ?, usuario = ? where id = ?;";
    private String stmtDelete = "DELETE FROM ResolucaoProva WhERE id = ?;";
    private String stmtVerificaResolucaoByProvaAndUsuario = "SELECT * FROM ResolucaoProva WHERE prova = ? AND usuario = ?;";
    private String stmtGetByProvaID = "select * From ResolucaoProva where prova = ?;";
    
    
    public List<ResolucaoProva> getResolucaoProvaByProvaId(int idProva) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtGetByProvaID);
            stmt.setInt(1, idProva);
            rs = stmt.executeQuery();
            List<ResolucaoProva> lstResProva = new ArrayList();

            while (rs.next()) {
                ResolucaoProva resProva = new ResolucaoProva();
                ResolucaoProvaDAO resProvaDAO = new ResolucaoProvaDAO();
                resProva = resProvaDAO.getById(rs.getInt("id"));
                
                lstResProva.add(resProva);
            }
            return lstResProva;

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
    
    public void deleteResolucaoProva(int id) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(stmtDelete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResolucaoProva verificaResolucao(int idProva, int idUsuario) throws ClassNotFoundException {
        ResolucaoProva resProva = new ResolucaoProva();
        ResolucaoProvaDAO resProvaDAO = new ResolucaoProvaDAO();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtVerificaResolucaoByProvaAndUsuario, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, idProva);
            stmt.setInt(2, idUsuario);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                resProva = resProvaDAO.getById(rs.getInt("id"));
            }
            return resProva;
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
    
    public ResolucaoProva getById(int id) throws ClassNotFoundException {
        ResolucaoProva resProva = new ResolucaoProva();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                resProva.setId(rs.getInt("id"));
                Prova prova = new Prova();
                ProvaDAO provaDAO = new ProvaDAO();
                prova = provaDAO.getById(rs.getInt("prova"));
                resProva.setProva(prova);
                
                Usuario usuario = new Usuario();
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                usuario = usuarioDAO.getById(rs.getInt("usuario"));
                resProva.setUsuario(usuario);
                
                resProva.setNotaFinal(rs.getFloat("notaFinal"));
                
            }
            return resProva;
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
    
    public void insert(ResolucaoProva resProva){
        PreparedStatement stmt = null;
        int idObjeto = 0;
        try {
            stmt = con.prepareStatement(stmtInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, resProva.getProva().getId());
            stmt.setFloat(2, resProva.getNotaFinal());
            stmt.setInt(3, resProva.getUsuario().getId());
            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
            }

            resProva.setId(idObjeto);

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
    
    public void update(ResolucaoProva resProva) {
        PreparedStatement stmt = null;
        try {
            if(resProva.getId() > 0 ){
                stmt = con.prepareStatement(stmtUpdate);
                stmt.setInt(1, resProva.getProva().getId());
                stmt.setFloat(2, resProva.getNotaFinal());
                stmt.setInt(3, resProva.getUsuario().getId());
                stmt.setInt(4, resProva.getId());
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
