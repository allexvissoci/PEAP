/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.NivelDificuldade;
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
public class NivelDificuldadeDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelectById = "select * From NivelDificuldade where id = ?;";
    private String stmtSelect = "SELECT * FROM NivelDificuldade;";
    
    public List<NivelDificuldade> getListaToSelect() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            List<NivelDificuldade> lstnivel = new ArrayList();
            stmt = con.prepareStatement(stmtSelect);
            rs = stmt.executeQuery();
            NivelDificuldade nivel = new NivelDificuldade();
            NivelDificuldadeDAO nivelDAO = new NivelDificuldadeDAO();
            while(rs.next()){
                nivel = nivelDAO.getById(rs.getInt("id"));
                lstnivel.add(nivel);
            }
            return lstnivel;
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

    public NivelDificuldade getById(int id) throws ClassNotFoundException {
        NivelDificuldade nivel = new NivelDificuldade();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                nivel.setId(rs.getInt("id"));
                nivel.setDescricao(rs.getString("descricao"));
            }
            return nivel;
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
