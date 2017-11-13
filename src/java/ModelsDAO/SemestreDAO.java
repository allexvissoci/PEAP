/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.Semestre;
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
public class SemestreDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelect = "select * from Semestre ";
    private String stmtSelectById = "select * From Semestre where id = ?;";
    private String stmtInsert = "insert into Semestre (semestreNome) values (?);";
    private String stmtUpdate = "update Semestre set semestreNome = ? where id = ?;";
    private String stmtDelete = "DELETE FROM Semestre WhERE id = ?;";
    private String stmtQtdeTurno = "select count(*) as cont from Turno";
    private String stmtCountSemestreTurma = "SELECT COUNT(*) as cont FROM Turma WHERE semestre = ? AND curso = ?";
    
    
    public List<Semestre> getLista() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelect);
            rs = stmt.executeQuery();
            List<Semestre> lstsemestre = new ArrayList();

            while (rs.next()) {
                Semestre semestre = new Semestre();
                semestre.setId(rs.getInt("id"));
                semestre.setSemestreNome(rs.getString("semestreNome"));
                
                lstsemestre.add(semestre);
            }
            return lstsemestre;

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
    
    public List<Semestre> getListaToSelect(int cursoId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        PreparedStatement stmtQtTurno = null;
        PreparedStatement stmtCompara = null;
        ResultSet rs = null;
        ResultSet rsQtTurno = null;
        ResultSet rsCompara = null;
        try {
            
            //// get quantidade de turnos;
            stmtQtTurno = con.prepareStatement(stmtQtdeTurno);
            rsQtTurno = stmtQtTurno.executeQuery();
            int quantidadeTurno = 0;
            while(rsQtTurno.next()){
                quantidadeTurno = rsQtTurno.getInt("cont");
            }
            
            
            List<Semestre> lstsemestre = new ArrayList();
            stmt = con.prepareStatement(stmtSelect);
            rs = stmt.executeQuery();
            Semestre semestre = new Semestre();
            SemestreDAO semestreDAO = new SemestreDAO();
            
            
            while(rs.next()){
                semestre = semestreDAO.getById(rs.getInt("id"));
                //// verifica quantos deste semestre tem em turma, se for igual ao numero de turnos cadastrados não adicionar
                stmtCompara = con.prepareStatement(stmtCountSemestreTurma);
                stmtCompara.setInt(1,semestre.getId());
                stmtCompara.setInt(2,cursoId);
                rsCompara = stmtCompara.executeQuery();
                int quantidadeSemestreNaTurma = 0;
                while(rsCompara.next()){
                    quantidadeSemestreNaTurma = rsCompara.getInt("cont");
                }
                
                if(quantidadeSemestreNaTurma < quantidadeTurno){
                    lstsemestre.add(semestre);
                }
            }
            return lstsemestre;
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
    
    public void deleteSemestre(int id) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(stmtDelete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Semestre getById(int id) throws ClassNotFoundException {
        Semestre semestre = new Semestre();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                semestre.setId(rs.getInt("id"));
                semestre.setSemestreNome(rs.getString("semestreNome"));
            }
            return semestre;
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
    
    public void insert(Semestre semestre){
        PreparedStatement stmt = null;
        int idObjeto = 0;
        try {
            stmt = con.prepareStatement(stmtInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, semestre.getSemestreNome());
            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
            }

            semestre.setId(idObjeto);

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
    
    public void update(Semestre semestre) {
        PreparedStatement stmt = null;
        try {
            if(semestre.getId() > 0 ){
                stmt = con.prepareStatement(stmtUpdate);
                stmt.setString(1, semestre.getSemestreNome());
                stmt.setInt(2, semestre.getId());
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
