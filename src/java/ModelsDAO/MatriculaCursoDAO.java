/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.Curso;
import Models.MatriculaCurso;
import Models.TipoUsuario;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author allex
 */
public class MatriculaCursoDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelectById = "select * from MatriculaCurso where id = ?";
    private String stmtInsertMatriculaCurso = "insert into MatriculaCurso (usuario, curso) values (?,?);";
    private String stmtUpdateMatriculaCurso = "UPDATE MatriculaCurso SET usuario = ? , curso = ? WHERE id = ?;";
    private String stmtDelete = "DELETE FROM MatriculaCurso WhERE id = ?;";
    private String stmtGetMatriculasCursos = "SELECT mc.id matriculaCursoId, u.nome usuarioNome, c.nome cursoNome FROM MatriculaCurso mc\n" +
                                            "INNER JOIN Usuario u\n" +
                                            "ON u.id = mc.usuario\n" +
                                            "INNER JOIN Curso c\n" +
                                            "ON c.id = mc.curso";

    public void insertMatriculaCurso(MatriculaCurso matricula){
        PreparedStatement stmt = null;
        int idObjeto = 0;
        try {
            stmt = con.prepareStatement(stmtInsertMatriculaCurso, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, matricula.getUsuario().getId());
            stmt.setInt(2, matricula.getCurso().getId());

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
    
    public MatriculaCurso getById(int id) throws ClassNotFoundException {
        MatriculaCurso matriculaCurso = new MatriculaCurso();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                
                matriculaCurso.setId(rs.getInt("id"));
                Usuario usuario = new Usuario();
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                usuario = usuarioDAO.getById(rs.getInt("usuario"));
                matriculaCurso.setUsuario(usuario);
                Curso curso = new Curso();
                CursoDAO cursoDAO = new CursoDAO();
                curso = cursoDAO.getById(rs.getInt("curso"));
                matriculaCurso.setCurso(curso);
                
            }
            return matriculaCurso;
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
    
    public List<MatriculaCurso> getMatriculasCursos() throws SQLException, ClassNotFoundException {
        MatriculaCurso matricula = new MatriculaCurso();
        MatriculaCursoDAO matriculaDAO = new MatriculaCursoDAO();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            List<MatriculaCurso> listMatriculaCursos = new ArrayList();
            stmt = con.prepareStatement(stmtGetMatriculasCursos);
            rs = stmt.executeQuery();
            while(rs.next()){
                matricula = matriculaDAO.getById(rs.getInt("matriculaCursoId"));
                listMatriculaCursos.add(matricula);
            }
            return listMatriculaCursos;
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
    
    public void deleteMatriculaCurso(int id) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(stmtDelete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update(MatriculaCurso matricula) {
        PreparedStatement stmt = null;
        try {
            if(matricula.getId() > 0 ){
                stmt = con.prepareStatement(stmtUpdateMatriculaCurso);
                stmt.setInt(1, matricula.getUsuario().getId());
                stmt.setInt(2, matricula.getCurso().getId());
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
