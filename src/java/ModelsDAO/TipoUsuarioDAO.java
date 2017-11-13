/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.TipoUsuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author allex
 */
public class TipoUsuarioDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtGetTipoUsuarios = "select * from TipoUsuario";
    private String stmtSelectById = "select * from TipoUsuario where id = ?";
    
    public List<TipoUsuario> getListaTipoUsuario() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtGetTipoUsuarios);
            rs = stmt.executeQuery();
            List<TipoUsuario> lstTipoUsuarios = new ArrayList();

            while (rs.next()) {
                // criando o objeto Usuario
                TipoUsuario tipoUsuario = new TipoUsuario();
                tipoUsuario.setId(rs.getInt("id"));
                tipoUsuario.setNomeTipo(rs.getString("nomeTipo"));
                
                lstTipoUsuarios.add(tipoUsuario);
            }
            return lstTipoUsuarios;

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
    
    
    public TipoUsuario getById(int id) throws ClassNotFoundException {
        TipoUsuario tipoUsuario = new TipoUsuario();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                
                tipoUsuario.setId(rs.getInt("id"));
                tipoUsuario.setNomeTipo(rs.getString("nomeTipo"));
                
            }
            return tipoUsuario;
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
