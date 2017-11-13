/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.Turno;
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
public class TurnoDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelectById = "SELECT * FROM Turno where id = ? ";
    private String stmtGetToSelect = "SELECT * FROM Turno\n" +
                                     "WHERE id NOT IN (SELECT turno FROM Turma WHERE curso = ? AND semestre = ?);";
    
    public Turno getById(int id) throws ClassNotFoundException {
        Turno turno = new Turno();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                
                turno.setId(rs.getInt("id"));
                turno.setTurnoNome(rs.getString("turno"));
                
            }
            return turno;
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
    
    public List<Turno> getListaToSelect(int cursoId, int semestreId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            List<Turno> lstturno = new ArrayList();
            stmt = con.prepareStatement(stmtGetToSelect);
            stmt.setInt(1, cursoId);
            stmt.setInt(2, semestreId);
            rs = stmt.executeQuery();
            Turno turno = new Turno();
            TurnoDAO turnoDAO = new TurnoDAO();
            while(rs.next()){
                turno = turnoDAO.getById(rs.getInt("id"));
                lstturno.add(turno);
            }
            return lstturno;
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
    
}
