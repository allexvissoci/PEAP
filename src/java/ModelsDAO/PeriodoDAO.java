/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.Periodo;
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
public class PeriodoDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelect = "select * from Periodo ";
    private String stmtSelectById = "select * From Periodo where id = ?;";
    private String stmtInsert = "insert into Periodo (periodoNome) values (?);";
    private String stmtUpdate = "update Periodo set periodoNome = ? where id = ?;";
    private String stmtDelete = "DELETE FROM Periodo WhERE id = ?;";
    private String stmtGetToSelect = "SELECT * FROM Periodo WHERE id NOT IN (SELECT periodo FROM TurmaPeriodo tp INNER JOIN Turma t ON t.id = tp.turma WHERE t.id = ?);";
    private String stmtAddPeriodoEmTurma = "insert into TurmaPeriodo (turma, periodo) values (?,?);";
    private String stmtRemoveDeTurma = "delete from TurmaPeriodo where turma = ? and periodo = ?;";
    private String stmtDeletaDisciplinasDoPeriodo = "delete from PeriodoDisciplina where turma = ? and periodo = ?;";
    
    public void removeDeTurma(Periodo periodo,Turma turma) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        PreparedStatement stmtRemoveDisciplinasDoPeriodo = null;
        try {
            
            stmtRemoveDisciplinasDoPeriodo = con.prepareStatement(stmtDeletaDisciplinasDoPeriodo);
            stmtRemoveDisciplinasDoPeriodo.setInt(1, turma.getId());
            stmtRemoveDisciplinasDoPeriodo.setInt(2, periodo.getId());
            stmtRemoveDisciplinasDoPeriodo.executeUpdate();
            
            stmt = con.prepareStatement(stmtRemoveDeTurma);
            stmt.setInt(1, turma.getId());
            stmt.setInt(2, periodo.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public List<Periodo> getListaToSelect(int turmaId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            List<Periodo> lstPeriodo = new ArrayList();
            stmt = con.prepareStatement(stmtGetToSelect);
            stmt.setInt(1, turmaId);
            rs = stmt.executeQuery();
            Periodo periodo = new Periodo();
            PeriodoDAO periodoDAO = new PeriodoDAO();
            while(rs.next()){
                periodo = periodoDAO.getById(rs.getInt("id"));
                lstPeriodo.add(periodo);
            }
            return lstPeriodo;
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
    
    public List<Periodo> getLista() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelect);
            rs = stmt.executeQuery();
            List<Periodo> lstperiodo = new ArrayList();

            while (rs.next()) {
                Periodo periodo = new Periodo();
                periodo.setId(rs.getInt("id"));
                periodo.setPeriodoNome(rs.getString("periodoNome"));
                
                lstperiodo.add(periodo);
            }
            return lstperiodo;

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
    
    public void deletePeriodo(int id) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(stmtDelete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Periodo getById(int id) throws ClassNotFoundException {
        Periodo periodo = new Periodo();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                periodo.setId(rs.getInt("id"));
                periodo.setPeriodoNome(rs.getString("periodoNome"));
            }
            return periodo;
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
    
    public void addPeriodoEmTurma(Periodo periodo, Turma turma){
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(stmtAddPeriodoEmTurma, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, turma.getId());
            stmt.setInt(2, periodo.getId());
            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();

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
    
    public void insert(Periodo periodo){
        PreparedStatement stmt = null;
        int idObjeto = 0;
        try {
            stmt = con.prepareStatement(stmtInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, periodo.getPeriodoNome());
            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
            }

            periodo.setId(idObjeto);

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
    
    public void update(Periodo periodo) {
        PreparedStatement stmt = null;
        try {
            if(periodo.getId() > 0 ){
                stmt = con.prepareStatement(stmtUpdate);
                stmt.setString(1, periodo.getPeriodoNome());
                stmt.setInt(2, periodo.getId());
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
