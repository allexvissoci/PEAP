/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.NivelDificuldade;
import Models.Questao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author allex
 */
public class QuestaoDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelectById = "select * From Questao where id = ?;";
    private String stmtInsert = "insert into Questao (enunciado, valor, nivelDificuldade) values (?, ?, ?);";
    private String stmtUpdate = "update Questao set enunciado = ?, valor = ?, nivelDificuldade = ? where id = ?;";
    private String stmtDelete = "DELETE FROM Questao WhERE id = ?;";
    
    private String stmtInsertProvaQuestao = "insert into ProvaQuestao (prova, questao) values (?, ?);";
    
    private String stmtselectByDisciplinaId = "SELECT q.* FROM Questao q\n" +
                                                "INNER JOIN ProvaQuestao pq\n" +
                                                "ON pq.questao = q.id\n" +
                                                "INNER JOIN Prova p\n" +
                                                "ON pq.prova = p.id\n" +
                                                "WHERE p.id = ?;";
    
    private String stmtDeleteProvaQuestao = "delete from ProvaQuestao where questao = ?;";
    
    private String stmtCheckAlternativa = "SELECT q.id questaoId, q.`valor` questaoValor , a.id alternativaId , a.`descricao` alternativaDescricao, a.`correta` alternativaCorreta\n" +
                                            "FROM Questao q\n" +
                                            "INNER JOIN ProvaQuestao pq \n" +
                                            "ON pq.questao = q.id\n" +
                                            "INNER JOIN Prova p\n" +
                                            "ON pq.prova = p.id\n" +
                                            "INNER JOIN Alternativa a\n" +
                                            "ON a.questao = q.id\n" +
                                            "WHERE  a.`correta` = TRUE AND a.id = ? or q.anulada = true;";
    
    private String stmtAnulaQuestao = "update Questao set anulada = ? where id = ?";
    private String stmtAnulaResolucaoQuestao = "update ResolucaoQuestao set anulada = ? where questao = ?";
    
    public JSONObject checkAlternativa(int alternativaId){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtCheckAlternativa);
            stmt.setInt(1, alternativaId);
            rs = stmt.executeQuery();
            
            JSONObject jsonObject = new JSONObject();
            while (rs.next()) {
                jsonObject.put("idQuestao", rs.getInt("questaoId"));
                jsonObject.put("valor", rs.getFloat("questaoValor"));
                jsonObject.put("idAlternativa", rs.getInt("alternativaId"));
            }
            return jsonObject;

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
    
    public List<Questao> getQuestoesByDisciplinaId(int provaId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtselectByDisciplinaId);
            stmt.setInt(1, provaId);
            rs = stmt.executeQuery();
            List<Questao> lstquestao = new ArrayList();

            while (rs.next()) {
                Questao questao = new Questao();
                questao.setId(rs.getInt("id"));
                questao.setEnunciado(rs.getString("enunciado"));
                questao.setValor(rs.getFloat("valor"));
                questao.setAnulada(rs.getBoolean("anulada"));
                NivelDificuldade nivel = new NivelDificuldade();
                NivelDificuldadeDAO nivelDAO = new NivelDificuldadeDAO();
                nivel = nivelDAO.getById(rs.getInt("nivelDificuldade"));
                questao.setNivelDificuldade(nivel);
                lstquestao.add(questao);
            }
            return lstquestao;

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
    
    public void deleteQuestao(int id) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        try {
            stmt2 = con.prepareStatement(stmtDeleteProvaQuestao);
            stmt2.setInt(1, id);
            stmt2.executeUpdate();
            
            stmt = con.prepareStatement(stmtDelete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Questao getById(int id) throws ClassNotFoundException {
        Questao questao = new Questao();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                questao.setId(rs.getInt("id"));
                questao.setEnunciado(rs.getString("enunciado"));
                questao.setValor(rs.getFloat("valor"));
                questao.setAnulada(rs.getBoolean("anulada"));
                NivelDificuldade nivel = new NivelDificuldade();
                NivelDificuldadeDAO nivelDAO = new NivelDificuldadeDAO();
                nivel = nivelDAO.getById(rs.getInt("nivelDificuldade"));
                questao.setNivelDificuldade(nivel);
            }
            return questao;
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
    
    public void insert(Questao questao, int provaId){
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        int idObjeto = 0;
        try {
            stmt = con.prepareStatement(stmtInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, questao.getEnunciado());
            stmt.setFloat(2, questao.getValor());
            stmt.setInt(3, questao.getNivelDificuldade().getId());
            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
            }
            questao.setId(idObjeto);
            
            stmt2 = con.prepareStatement(stmtInsertProvaQuestao, Statement.RETURN_GENERATED_KEYS);
            stmt2.setInt(1, provaId);
            stmt2.setInt(2, questao.getId());
            stmt2.execute();

            
            
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
    
    public void anulaQuestao(int questaoId, Boolean anular) {
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        try {
            stmt = con.prepareStatement(stmtAnulaQuestao);
            stmt.setBoolean(1, anular);
            stmt.setInt(2, questaoId);
            stmt.executeUpdate();
            
            stmt2 = con.prepareStatement(stmtAnulaResolucaoQuestao);
            stmt2.setBoolean(1, anular);
            stmt2.setInt(2, questaoId);
            stmt2.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                stmt.close();
                stmt2.close();
            } catch (Exception ex) {
                System.out.println("Erro ao fechar stmt. Ex=" + ex.getMessage());
            };
            
        }
    }    
    
    public void update(Questao questao) {
        PreparedStatement stmt = null;
        try {
            if(questao.getId() > 0 ){
                stmt = con.prepareStatement(stmtUpdate);
                stmt.setString(1, questao.getEnunciado());
                stmt.setFloat(2, questao.getValor());
                stmt.setInt(3, questao.getNivelDificuldade().getId());
                stmt.setInt(4, questao.getId());
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
