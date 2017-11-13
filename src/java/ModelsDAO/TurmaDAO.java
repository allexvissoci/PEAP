/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.Semestre;
import Models.Turma;
import Models.Turno;
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
public class TurmaDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelect = "select * from Turma ";
    private String stmtSelectById = "select * From Turma where id = ?;";
    private String stmtInsert = "insert into Turma (nome, curso, turno, semestre) values (?, ?, ?, ?);";
    private String stmtUpdate = "update Turma set nome = ?, curso = ?, turno = ?, semestre = ? where id = ?;";
    private String stmtDelete = "DELETE FROM Turma WhERE id = ?;";
    
    
    public List<Turma> getLista() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelect);
            rs = stmt.executeQuery();
            List<Turma> lstturma = new ArrayList();

            while (rs.next()) {
                Turma turma = new Turma();
                turma.setId(rs.getInt("id"));
                turma.setNome(rs.getString("nome"));
                
                Turno turno = new Turno();
                TurnoDAO turnoDAO = new TurnoDAO();
                turno = turnoDAO.getById(rs.getInt("turno"));
                turma.setTurno(turno);
                
                Semestre semestre = new Semestre();
                SemestreDAO semestreDAO = new SemestreDAO();
                semestre = semestreDAO.getById(rs.getInt("semestre"));
                turma.setSemestre(semestre);
                
                lstturma.add(turma);
            }
            return lstturma;

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
    
    public void deleteTurma(int id) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        try {
            stmt2 = con.prepareStatement("DELETE FROM TurmaPeriodo WHERE turma = ?;");
            stmt2.setInt(1, id);
            stmt2.executeUpdate();
            
            stmt = con.prepareStatement(stmtDelete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Turma getById(int id) throws ClassNotFoundException {
        Turma turma = new Turma();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                turma.setId(rs.getInt("id"));
                turma.setNome(rs.getString("nome"));
                
                Turno turno = new Turno();
                TurnoDAO turnoDAO = new TurnoDAO();
                turno = turnoDAO.getById(rs.getInt("turno"));
                turma.setTurno(turno);
                
                Semestre semestre = new Semestre();
                SemestreDAO semestreDAO = new SemestreDAO();
                semestre = semestreDAO.getById(rs.getInt("semestre"));
                turma.setSemestre(semestre);
            }
            return turma;
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
    
    public void insert(Turma turma){
        PreparedStatement stmt = null;
        int idObjeto = 0;
        try {
            stmt = con.prepareStatement(stmtInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, turma.getNome());
            stmt.setInt(2, turma.getCurso().getId());
            stmt.setInt(3, turma.getTurno().getId());
            stmt.setInt(4, turma.getSemestre().getId());
            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
            }

            turma.setId(idObjeto);

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
    
    public void update(Turma turma) {
        PreparedStatement stmt = null;
        try {
            if(turma.getId() > 0 ){
                stmt = con.prepareStatement(stmtUpdate);
                stmt.setString(1, turma.getNome());
                stmt.setInt(2, turma.getCurso().getId());
                stmt.setInt(3, turma.getTurno().getId());
                stmt.setInt(4, turma.getSemestre().getId());
                stmt.setInt(5, turma.getId());
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
