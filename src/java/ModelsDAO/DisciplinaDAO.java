/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.Curso;
import Models.Disciplina;
import Models.Periodo;
import Models.Turma;
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
public class DisciplinaDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelect = "select * from Disciplina ";
    private String stmtSelectById = "select * From Disciplina where id = ?;";
    private String stmtInsert = "insert into Disciplina (nome, curso) values (?, ?);";
    private String stmtUpdate = "update Disciplina set nome = ?, curso = ? where id = ?;";
    private String stmtDelete = "DELETE FROM Disciplina WhERE id = ?;";
    private String stmtGetToAddDisciplinaEmPeriodo = "SELECT * FROM Disciplina d WHERE id NOT IN (\n" +
                                                    "	SELECT d.id FROM Periodo p\n" +
                                                    "	INNER JOIN TurmaPeriodo tp\n" +
                                                    "	ON tp.periodo = p.id\n" +
                                                    "	INNER JOIN Turma t\n" +
                                                    "	ON tp.turma = t.id\n" +
                                                    "	INNER JOIN PeriodoDisciplina pd\n" +
                                                    "	ON pd.periodo = p.id\n" +
                                                    "	INNER JOIN Disciplina d\n" +
                                                    "	ON pd.disciplina = d.id\n" +
                                                    "	WHERE t.id = ? AND p.id = ?) and d.curso = ?;";
    
    private String stmtAddDisciplinaEmPeriodo = "insert into PeriodoDisciplina (periodo, disciplina, turma) values (?,?,?);";
    private String stmtRemoveDePeriodo = "delete from PeriodoDisciplina where periodo = ? and disciplina = ? and turma = ?";
    
    
    public void addDisciplinaEmPeriodo(Periodo periodo, Turma turma, Disciplina disciplina){
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(stmtAddDisciplinaEmPeriodo, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, periodo.getId());
            stmt.setInt(2, disciplina.getId());
            stmt.setInt(3, turma.getId());
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
    
    public List<Disciplina> getListaToAddEmPeriodoSelect(int turmaId, int periodoId, int cursoId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            List<Disciplina> lstdisciplina = new ArrayList();
            stmt = con.prepareStatement(stmtGetToAddDisciplinaEmPeriodo);
            stmt.setInt(1, turmaId);
            stmt.setInt(2, periodoId);
            stmt.setInt(3, cursoId);
            rs = stmt.executeQuery();
            Disciplina disciplina = new Disciplina();
            DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
            while(rs.next()){
                disciplina = disciplinaDAO.getById(rs.getInt("id"));
                lstdisciplina.add(disciplina);
            }
            return lstdisciplina;
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
    
    public List<Disciplina> getListaToSelect(int cursoId, int usuarioId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            List<Disciplina> lstdisciplina = new ArrayList();
            
            if(usuarioId > 0){
                stmt = con.prepareStatement(stmtSelect + "where id NOT IN (SELECT disciplina FROM MatriculaDisciplina WHERE usuario = ?) AND curso = ?;");
                stmt.setInt(1, usuarioId);
                stmt.setInt(2, cursoId);

            }else{
                stmt = con.prepareStatement(stmtSelect + "where curso = ?;");
                stmt.setInt(1, cursoId);
                
            }
            rs = stmt.executeQuery();
            Disciplina disciplina = new Disciplina();
            DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
            while(rs.next()){
                disciplina = disciplinaDAO.getById(rs.getInt("id"));
                lstdisciplina.add(disciplina);
            }
            return lstdisciplina;
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
    
    public List<Disciplina> getLista() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelect);
            rs = stmt.executeQuery();
            List<Disciplina> lstdisciplina = new ArrayList();

            while (rs.next()) {
                Disciplina disciplina = new Disciplina();
                disciplina.setId(rs.getInt("id"));
                disciplina.setNome(rs.getString("nome"));
                Curso curso = new Curso();
                CursoDAO cursoDAO = new CursoDAO();
                curso = cursoDAO.getById(rs.getInt("curso"));
                disciplina.setCurso(curso);
                lstdisciplina.add(disciplina);
            }
            return lstdisciplina;

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
    
    public void removeDePeriodo(Periodo periodo,Disciplina disciplina,Turma turma) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(stmtRemoveDePeriodo);
            stmt.setInt(1, periodo.getId());
            stmt.setInt(2, disciplina.getId());
            stmt.setInt(3, turma.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteDisciplina(int id) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(stmtDelete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Disciplina getById(int id) throws ClassNotFoundException {
        Disciplina disciplina = new Disciplina();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                disciplina.setId(rs.getInt("id"));
                disciplina.setNome(rs.getString("nome"));
                Curso curso = new Curso();
                CursoDAO cursoDAO = new CursoDAO();
                curso = cursoDAO.getById(rs.getInt("curso"));
                disciplina.setCurso(curso);
            }
            return disciplina;
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
    
    public void insert(Disciplina disciplina){
        PreparedStatement stmt = null;
        int idObjeto = 0;
        try {
            stmt = con.prepareStatement(stmtInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, disciplina.getNome());
            stmt.setInt(2, disciplina.getCurso().getId());
            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
            }

            disciplina.setId(idObjeto);

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
    
    public void update(Disciplina disciplina) {
        PreparedStatement stmt = null;
        try {
            if(disciplina.getId() > 0 ){
                stmt = con.prepareStatement(stmtUpdate);
                stmt.setString(1, disciplina.getNome());
                stmt.setInt(2, disciplina.getCurso().getId());
                stmt.setInt(3, disciplina.getId());
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
