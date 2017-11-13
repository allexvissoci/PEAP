/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.Disciplina;
import Models.MatriculaDisciplina;
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
public class MatriculaDisciplinaDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelectById = "select * from MatriculaDisciplina where id = ?";
    private String stmtInsertMatriculaDisciplina = "insert into MatriculaDisciplina (usuario, disciplina) values (?,?);";
    private String stmtUpdateMatriculaDisciplina = "UPDATE MatriculaDisciplina SET usuario = ? , disciplina = ? WHERE id = ?;";
    private String stmtDelete = "DELETE FROM MatriculaDisciplina WhERE id = ?;";
    private String stmtGetMatriculasDisciplina = "SELECT md.id matriculaDisciplinaId, u.nome usuarioNome, d.nome disciplinaNome FROM MatriculaDisciplina md\n" +
                                            "INNER JOIN Usuario u\n" +
                                            "ON u.id = md.usuario\n" +
                                            "INNER JOIN Disciplina d\n" +
                                            "ON d.id = md.disciplina";

    public void insertMatriculaDisciplina(MatriculaDisciplina matricula){
        PreparedStatement stmt = null;
        int idObjeto = 0;
        try {
            stmt = con.prepareStatement(stmtInsertMatriculaDisciplina, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, matricula.getUsuario().getId());
            stmt.setInt(2, matricula.getDisciplina().getId());

            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
            }

            matricula.setId(idObjeto);

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
    
    public MatriculaDisciplina getById(int id) throws ClassNotFoundException {
        MatriculaDisciplina matriculaDisciplina = new MatriculaDisciplina();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                
                matriculaDisciplina.setId(rs.getInt("id"));
                Usuario usuario = new Usuario();
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                usuario = usuarioDAO.getById(rs.getInt("usuario"));
                matriculaDisciplina.setUsuario(usuario);
                Disciplina disciplina = new Disciplina();
                DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
                disciplina = disciplinaDAO.getById(rs.getInt("disciplina"));
                matriculaDisciplina.setDisciplina(disciplina);
                
            }
            return matriculaDisciplina;
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
    
    public List<MatriculaDisciplina> getMatriculasDisciplinas() throws SQLException, ClassNotFoundException {
        MatriculaDisciplina matricula = new MatriculaDisciplina();
        MatriculaDisciplinaDAO matriculaDAO = new MatriculaDisciplinaDAO();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            List<MatriculaDisciplina> listMatriculaDisciplinas = new ArrayList();
            stmt = con.prepareStatement(stmtGetMatriculasDisciplina);
            rs = stmt.executeQuery();
            while(rs.next()){
                matricula = matriculaDAO.getById(rs.getInt("matriculaDisciplinaId"));
                listMatriculaDisciplinas.add(matricula);
            }
            return listMatriculaDisciplinas;
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
    
    public void deleteMatriculaDisciplina(int id) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(stmtDelete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update(MatriculaDisciplina matricula) {
        PreparedStatement stmt = null;
        try {
            if(matricula.getId() > 0 ){
                stmt = con.prepareStatement(stmtUpdateMatriculaDisciplina);
                stmt.setInt(1, matricula.getUsuario().getId());
                stmt.setInt(2, matricula.getDisciplina().getId());
                stmt.setInt(3, matricula.getId());
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
