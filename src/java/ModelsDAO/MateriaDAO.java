/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.Materia;
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
public class MateriaDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelect = "select * from Materia ";
    private String stmtSelectById = "select * From Materia where id = ?;";
    private String stmtInsert = "insert into Materia (nome) values (?);";
    private String stmtUpdate = "update Materia set nome = ? where id = ?;";
    private String stmtDelete = "DELETE FROM Materia WhERE id = ?;";
    private String stmtSelectByDisciplinaId = "SELECT m.* FROM Materia m\n" +
                                                "INNER JOIN DisciplinaMateria dm\n" +
                                                "ON dm.materia = m.id\n" +
                                                "INNER JOIN Disciplina d\n" +
                                                "ON dm.disciplina = d.id\n" +
                                                "WHERE d.id = ?";
    private String stmtInsertDisciplinaMateria = "insert into DisciplinaMateria (disciplina, materia) values (?, ?);";
    private String stmtDeleteDisciplinaMateria = "delete from DisciplinaMateria where materia = ?;";
    
    
    public List<Materia> getLista() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelect);
            rs = stmt.executeQuery();
            List<Materia> lstmateria = new ArrayList();

            while (rs.next()) {
                Materia materia = new Materia();
                materia.setId(rs.getInt("id"));
                materia.setNome(rs.getString("nome"));
                lstmateria.add(materia);
            }
            return lstmateria;

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
    
    public List<Materia> getMateriasByDisciplinaId(int disciplinaId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectByDisciplinaId);
            stmt.setInt(1, disciplinaId);
            rs = stmt.executeQuery();
            List<Materia> lstmateria = new ArrayList();

            while (rs.next()) {
                Materia materia = new Materia();
                materia.setId(rs.getInt("id"));
                materia.setNome(rs.getString("nome"));
                lstmateria.add(materia);
            }
            return lstmateria;

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
    
    public void deleteMateria(int id) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        try {
            
            stmt = con.prepareStatement(stmtDeleteDisciplinaMateria);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
            stmt2 = con.prepareStatement(stmtDelete);
            stmt2.setInt(1, id);
            stmt2.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Materia getById(int id) throws ClassNotFoundException {
        Materia materia = new Materia();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                materia.setId(rs.getInt("id"));
                materia.setNome(rs.getString("nome"));
            }
            return materia;
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
    
    public void insert(Materia materia, int disciplinaId){
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        int idObjeto = 0;
        try {
            stmt = con.prepareStatement(stmtInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, materia.getNome());
            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
            }

            materia.setId(idObjeto);
            
            stmt2 = con.prepareStatement(stmtInsertDisciplinaMateria, Statement.RETURN_GENERATED_KEYS);
            stmt2.setInt(1, disciplinaId);
            stmt2.setInt(2, materia.getId());
            stmt2.execute();
            rs = stmt2.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
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
    
    public void update(Materia materia) {
        PreparedStatement stmt = null;
        try {
            if(materia.getId() > 0 ){
                stmt = con.prepareStatement(stmtUpdate);
                stmt.setString(1, materia.getNome());
                stmt.setInt(2, materia.getId());
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
