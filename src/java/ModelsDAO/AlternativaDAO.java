/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.Alternativa;
import Models.ConnectionFactory;
import Models.Questao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author allex
 */
public class AlternativaDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelectById = "select * From Alternativa where id = ?;";
    private String stmtInsert = "insert into Alternativa (descricao, correta, questao) values (?, ?, ?);";
    private String stmtUpdate = "update Alternativa set descricao = ?, correta = ?, questao = ? where id = ?;";
    private String stmtDelete = "DELETE FROM Alternativa WhERE id = ?;";
    private String stmtDeleteByQuestao = "DELETE FROM Alternativa WhERE questao = ?;";
    
    private String stmtSelectByQuestaoId = "select * from Alternativa where questao = ?;";
    
    private String stmtSelectCorretaByQuestao = "select * from Alternativa where questao = ? and correta = true limit 1;";
    
    public Alternativa getAlternativaCorretaByQuestao(int questaoId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectCorretaByQuestao);
            stmt.setInt(1, questaoId);
            rs = stmt.executeQuery();
            Alternativa alternativa = new Alternativa();
            AlternativaDAO alternativaDAO = new AlternativaDAO();
            while(rs.next()){
                alternativa = alternativaDAO.getById(rs.getInt("id"));
            }
            return alternativa;


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
    
    public JSONArray getAlternativasByQuestao(int questaoId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectByQuestaoId);
            stmt.setInt(1, questaoId);
            rs = stmt.executeQuery();
            JSONArray jsonarray = new JSONArray();
            while(rs.next()){
                
                JSONObject object = new JSONObject();
                object.put("alternativaId", rs.getInt("id"));
                object.put("descricao", rs.getString("descricao"));
                object.put("correta", rs.getBoolean("correta"));
                object.put("questaoId", rs.getInt("questao"));
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
    
    public void deleteByQuestao(int idQuestao) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(stmtDeleteByQuestao);
            stmt.setInt(1, idQuestao);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteAlternativa(int id) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(stmtDelete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Alternativa getById(int id) throws ClassNotFoundException {
        Alternativa alternativa = new Alternativa();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                alternativa.setId(rs.getInt("id"));
                alternativa.setDescricao(rs.getString("descricao"));
                alternativa.setCorreta(rs.getBoolean("correta"));
                Questao questao = new Questao();
                QuestaoDAO questaoDAO = new QuestaoDAO();
                questao = questaoDAO.getById(rs.getInt("questao"));
                alternativa.setQuestao(questao);
            }
            return alternativa;
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
    
    public void insert(Alternativa alternativa){
        PreparedStatement stmt = null;
        int idObjeto = 0;
        try {
            stmt = con.prepareStatement(stmtInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, alternativa.getDescricao());
            stmt.setBoolean(2, alternativa.getCorreta());
            stmt.setInt(3, alternativa.getQuestao().getId());
            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
            }

            alternativa.setId(idObjeto);

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
    
    public void update(Alternativa alternativa) {
        PreparedStatement stmt = null;
        try {
            if(alternativa.getId() > 0 ){
                stmt = con.prepareStatement(stmtUpdate);
                stmt.setString(1, alternativa.getDescricao());
                stmt.setBoolean(2, alternativa.getCorreta());
                stmt.setInt(3, alternativa.getQuestao().getId());
                stmt.setInt(4, alternativa.getId());
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
