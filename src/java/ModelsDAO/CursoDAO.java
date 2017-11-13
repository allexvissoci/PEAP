/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.Curso;
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
public class CursoDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelect = "SELECT c.* FROM Curso c\n" +
                                "INNER JOIN MatriculaCurso mc\n" +
                                "ON c.id = mc.`curso`\n" +
                                "INNER JOIN Usuario u\n" +
                                "ON u.id = mc.`usuario`\n" +
                                "WHERE u.id = ?;";
    
    private String stmtSelectById = "SELECT * FROM Curso WHERE id = ?";
    private String stmtInsert = "INSERT INTO Curso (nome) VALUES (?)";
    private String stmtUpdate = "UPDATE Curso SET nome = ? WHERE id = ?;";
    private String stmtDelete = "DELETE FROM Curso WhERE id = ?;";
    
    private String stmtGetCursoTurma =  "SELECT t.id AS turmaId, t.nome AS turmaNome, t.curso AS curso, t.turno AS turno,  s.id AS semestreId, s.semestreNome AS semestreNome \n" +
                                        "FROM Turma t\n" +
                                        "INNER JOIN Semestre s\n" +
                                        "ON t.semestre = s.id\n" +
                                        "WHERE curso = ?";
    
    private String stmtGetTurno =  "SELECT tur.id turnoId, tur.turno turnoNome, ANY_VALUE(t.id) turmaId, s.id semestreId FROM Turma as t\n" +
                                    "INNER JOIN Semestre as s\n" +
                                    "ON t.semestre = s.id\n" +
                                    "INNER JOIN Turno as tur\n" +
                                    "ON tur.id = t.turno\n" +
                                    "WHERE t.curso = ?\n" +
                                    "GROUP BY s.id, tur.id;";
    
    private String stmtGetPeriodo = "SELECT p.id periodoId, p.periodoNome periodoNome FROM Turma t\n" +
                                    "INNER JOIN TurmaPeriodo tp\n" +
                                    "ON tp.turma = t.id\n" +
                                    "INNER JOIN Periodo p\n" +
                                    "ON tp.periodo = p.id\n" +
                                    "WHERE t.id = ? AND t.turno = ?;";
    
    private String stmtGetDisciplina = "SELECT d.id AS disciplinaId, d.nome AS disciplinaNome FROM Disciplina d\n" +
                                        "INNER JOIN PeriodoDisciplina pd\n" +
                                        "ON pd.disciplina = d.id\n" +
                                        "INNER JOIN Periodo p\n" +
                                        "ON pd.periodo = p.id\n" +
                                        "WHERE d.curso = ? AND p.id = ? and pd.turma = ?";

    public List<Curso> getListaToSelect(Usuario usuario, int usuarioSelectedId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            List<Curso> lstcurso = new ArrayList();
            if(usuario.getTipoUsuario().getNomeTipo().equals("Administrador")){
                if(usuarioSelectedId > 0){
                    stmtSelect = "select * from Curso WHERE id NOT IN (SELECT curso FROM MatriculaCurso WHERE usuario = ?);";
                    stmt = con.prepareStatement(stmtSelect);
                    stmt.setInt(1, usuarioSelectedId);
                    
                }else{
                    stmtSelect = "select * from Curso";
                    stmt = con.prepareStatement(stmtSelect);
                }
            }else{
                stmt = con.prepareStatement(stmtSelect);
                stmt.setInt(1, usuario.getId());
            }
            rs = stmt.executeQuery();
            Curso curso = new Curso();
            CursoDAO cursoDAO = new CursoDAO();
            while(rs.next()){
                curso = cursoDAO.getById(rs.getInt("id"));
                lstcurso.add(curso);
            }
            return lstcurso;
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
    
    public List<Curso> getLista(Usuario usuario) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if(usuario.getTipoUsuario().getNomeTipo().equals("Administrador")){
                stmtSelect = "select * from Curso";
                stmt = con.prepareStatement(stmtSelect);
            }else{
                stmt = con.prepareStatement(stmtSelect);
                stmt.setInt(1, usuario.getId());
            }
            rs = stmt.executeQuery();
            List<Curso> lstCurso = new ArrayList();

            while (rs.next()) {
                // criando o objeto Usuario
                Curso curso = new Curso();
                curso.setId(rs.getInt("id"));
                curso.setNome(rs.getString("nome"));
                
                lstCurso.add(curso);
            }
            return lstCurso;

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
    
    public JSONArray getTurmas(int cursoId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtGetCursoTurma);
            stmt.setInt(1, cursoId);
            rs = stmt.executeQuery();
            JSONArray jsonarray = new JSONArray();
            while(rs.next()){
                
                JSONObject object = new JSONObject();
                object.put("turmaId", rs.getInt("turmaId"));
                object.put("turmaNome", rs.getString("turmaNome"));
                object.put("curso", rs.getInt("curso"));
                object.put("turno", rs.getInt("turno"));
                object.put("semestreId", rs.getInt("semestreId"));
                object.put("semestreNome", rs.getString("semestreNome"));                
                jsonarray.add(object);

            }
            return jsonarray;


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
    
    public JSONArray getTurnos(int cursoId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtGetTurno);
            stmt.setInt(1, cursoId);
            rs = stmt.executeQuery();
            JSONArray jsonarray = new JSONArray();
            while(rs.next()){
                
                JSONObject object = new JSONObject();
                object.put("turnoId", rs.getInt("turnoId"));
                object.put("turnoNome", rs.getString("turnoNome"));
                object.put("turmaId", rs.getInt("turmaId"));
                object.put("semestreId", rs.getInt("semestreId"));
                jsonarray.add(object);

            }
            return jsonarray;


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
    
    public JSONArray getPeriodos(int turmaId, int turnoId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtGetPeriodo);
            stmt.setInt(1, turmaId);
            stmt.setInt(2, turnoId);
            rs = stmt.executeQuery();
            JSONArray jsonarray = new JSONArray();
            while(rs.next()){
                JSONObject object = new JSONObject();
                object.put("periodoId", rs.getInt("periodoId"));
                object.put("periodoNome", rs.getString("periodoNome"));           
                jsonarray.add(object);
            }
            return jsonarray;
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
    
    public JSONArray getDisciplinas(int cursoId, int periodoId, int turmaId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtGetDisciplina);
            stmt.setInt(1, cursoId);
            stmt.setInt(2, periodoId);
            stmt.setInt(3, turmaId);
            rs = stmt.executeQuery();
            JSONArray jsonarray = new JSONArray();
            while(rs.next()){
                JSONObject object = new JSONObject();
                object.put("disciplinaId", rs.getInt("disciplinaId"));
                object.put("disciplinaNome", rs.getString("disciplinaNome"));           
                jsonarray.add(object);
            }
            return jsonarray;
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
    
    public void deleteCurso(int id) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        
        try {
            stmt = con.prepareStatement("DELETE FROM Disciplina WHERE curso = ?;");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
            stmt2 = con.prepareStatement("DELETE FROM Turma WHERE curso = ?;");
            stmt2.setInt(1, id);
            stmt2.executeUpdate();
            
            stmt3 = con.prepareStatement(stmtDelete);
            stmt3.setInt(1, id);
            stmt3.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    public Curso getById(int id) throws ClassNotFoundException {
        Curso curso = new Curso();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                
                curso.setId(rs.getInt("id"));
                curso.setNome(rs.getString("nome"));
                
            }
            return curso;
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
    
    public void update(Curso usuario) {
        PreparedStatement stmt = null;
        try {
            if(usuario.getId() > 0 ){
                stmt = con.prepareStatement(stmtUpdate);
                stmt.setString(1, usuario.getNome());
                stmt.setInt(2, usuario.getId());
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
    
    public void insert(Curso curso){
        PreparedStatement stmt = null;
        int idObjeto = 0;
        try {
            stmt = con.prepareStatement(stmtInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, curso.getNome());

            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
            }

            curso.setId(idObjeto);

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
