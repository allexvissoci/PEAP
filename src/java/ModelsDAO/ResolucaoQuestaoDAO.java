/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.Alternativa;
import Models.ConnectionFactory;
import Models.Questao;
import Models.ResolucaoProva;
import Models.ResolucaoQuestao;
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
public class ResolucaoQuestaoDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelectById = "select * From ResolucaoQuestao where id = ?;";
    private String stmtInsert = "insert into ResolucaoQuestao (questao, alternativaSelecionada, alternativaCorreta, resolucaoProva) values (?, ?, ?, ?);";
    private String stmtUpdate = "update ResolucaoQuestao set questao = ?, alternativaSelecionada = ?, alternativaCorreta = ?, resolucaoProva = ? where id = ?;";
    private String stmtDelete = "DELETE FROM ResolucaoQuestao WhERE id = ?;";
    
    private String stmtgetResolucaoByProvaAndQuestao = "SELECT rq.* FROM ResolucaoQuestao rq\n" +
                                                        "INNER JOIN ResolucaoProva rp\n" +
                                                        "ON rq.resolucaoProva = rp.id\n" +
                                                        "WHERE rp.prova = ? AND rq.questao = ? and rp.usuario = ?;";
    
    private String stmtGetByResProvID = "select * From ResolucaoQuestao where resolucaoProva = ?;";
    private String stmtgetRelatorioProva = "SELECT * FROM ResolucaoProva WHERE prova = ?;";
    
    public List<ResolucaoQuestao> getResolucaoQuestaoByResProva(int ResProvaId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtGetByResProvID);
            stmt.setInt(1, ResProvaId);
            rs = stmt.executeQuery();
            List<ResolucaoQuestao> lstResQuestao = new ArrayList();

            while (rs.next()) {
                ResolucaoQuestao resQuestao = new ResolucaoQuestao();
                ResolucaoQuestaoDAO resQuestaoDAO = new ResolucaoQuestaoDAO();
                resQuestao = resQuestaoDAO.getById(rs.getInt("id"));
                
                lstResQuestao.add(resQuestao);
            }
            return lstResQuestao;

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
    public void deleteResolucaoQuestao(int id) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(stmtDelete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<ResolucaoProva> getRelatorioProva(int idProva) throws ClassNotFoundException {
        ResolucaoProva resProva = new ResolucaoProva();
        ResolucaoProvaDAO resProvaDAO = new ResolucaoProvaDAO();
        PreparedStatement stmt = null;
        List<ResolucaoProva> lstRelatorio = new ArrayList();
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtgetRelatorioProva, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, idProva);
            rs = stmt.executeQuery();
            while (rs.next()) {
                resProva = resProvaDAO.getById(rs.getInt("id"));
                lstRelatorio.add(resProva);
            }
            return lstRelatorio;
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
    
    public ResolucaoQuestao getResolucaoByProvaAndQuestao(int idProva, int idQuestao, int idUsuario) throws ClassNotFoundException {
        ResolucaoQuestao resQuestao = new ResolucaoQuestao();
        ResolucaoQuestaoDAO resQuestaoDAO = new ResolucaoQuestaoDAO();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtgetResolucaoByProvaAndQuestao, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, idProva);
            stmt.setInt(2, idQuestao);
            stmt.setInt(3, idUsuario);
            rs = stmt.executeQuery();
            while (rs.next()) {
                resQuestao = resQuestaoDAO.getById(rs.getInt("id"));
            }
            return resQuestao;
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
    
    public ResolucaoQuestao getById(int id) throws ClassNotFoundException {
        ResolucaoQuestao resQuestao = new ResolucaoQuestao();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                resQuestao.setId(rs.getInt("id"));
                Questao questao = new Questao();
                QuestaoDAO questaoDAO = new QuestaoDAO();
                questao = questaoDAO.getById(rs.getInt("questao"));
                resQuestao.setQuestao(questao);
                
                AlternativaDAO alternativaDAO = new AlternativaDAO();
                Alternativa alternativaSelecionada = new Alternativa();
                alternativaSelecionada = alternativaDAO.getById(rs.getInt("alternativaSelecionada"));
                resQuestao.setAlternativaSelecionada(alternativaSelecionada);
                
                Alternativa alternativaCorreta = new Alternativa();
                alternativaCorreta = alternativaDAO.getById(rs.getInt("alternativaCorreta"));
                resQuestao.setAlternativaCorreta(alternativaCorreta);
                
                ResolucaoProva resProva = new ResolucaoProva();
                ResolucaoProvaDAO resProvaDAO = new ResolucaoProvaDAO();
                resProva = resProvaDAO.getById(rs.getInt("resolucaoProva"));
                resQuestao.setResolucaoProva(resProva);
                
                resQuestao.setAnulada(rs.getBoolean("anulada"));
            }
            return resQuestao;
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
    
    public void insert(ResolucaoQuestao resQuestao){
        PreparedStatement stmt = null;
        int idObjeto = 0;
        try {
            stmt = con.prepareStatement(stmtInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, resQuestao.getQuestao().getId());
            stmt.setInt(2, resQuestao.getAlternativaSelecionada().getId());
            stmt.setInt(3, resQuestao.getAlternativaCorreta().getId());
            stmt.setInt(4, resQuestao.getResolucaoProva().getId());
            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
            }

            resQuestao.setId(idObjeto);

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
    
    public void update(ResolucaoQuestao resQuestao) {
        PreparedStatement stmt = null;
        try {
            if(resQuestao.getId() > 0 ){
                stmt = con.prepareStatement(stmtUpdate);
                stmt.setInt(1, resQuestao.getQuestao().getId());
                stmt.setInt(2, resQuestao.getAlternativaSelecionada().getId());
                stmt.setInt(3, resQuestao.getAlternativaCorreta().getId());
                stmt.setInt(4, resQuestao.getResolucaoProva().getId());
                stmt.setInt(5, resQuestao.getId());
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
