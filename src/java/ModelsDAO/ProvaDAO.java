/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ConnectionFactory;
import Models.Prova;
import Models.ResolucaoProva;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author allex
 */
public class ProvaDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelectById = "select * From Prova where id = ?;";
    private String stmtInsert = "insert into Prova (descricao, observacao, dataAplicacao) values (?, ?, ?);";
    private String stmtUpdate = "update Prova set descricao = ?, observacao = ?, dataAplicacao = ? where id = ?;";
    private String stmtDelete = "DELETE FROM Prova WhERE id = ?;";
    private String stmtInsertDisciplinaProva = "insert into DisciplinaProva (disciplina, prova) values (?, ?)";
    

    private String stmtSelectByDisciplinaId = "SELECT p.id provaId, p.descricao descricao, p.observacao observacao, p.dataAplicacao dataAplicacao, curdate() dataAtual FROM Prova p\n" +
                                                "INNER JOIN DisciplinaProva dp\n" +
                                                "ON dp.prova = p.id \n" +
                                                "INNER JOIN Disciplina d\n" +
                                                "ON dp.disciplina = d.id\n" +
                                                "WHERE d.id = ? ";
    
    private String stmtDeleteDisciplinaProva = "delete from DisciplinaProva where prova = ?;";
    
    
    public JSONArray getProvasByDisciplinaId(int disciplinaId, String tipoUsuario, int usuarioId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if(tipoUsuario.equals("Aluno")){
                stmtSelectByDisciplinaId = stmtSelectByDisciplinaId + " and p.dataAplicacao >= CURDATE();";
            }
            
            stmt = con.prepareStatement(stmtSelectByDisciplinaId);
            stmt.setInt(1, disciplinaId);
            rs = stmt.executeQuery();
            JSONArray arrayProvas = new JSONArray();

            ResolucaoProva resolucaoProva = new ResolucaoProva();
            ResolucaoProvaDAO resolucaoProvaDAO = new ResolucaoProvaDAO();
            while (rs.next()) {
                JSONObject jsonProva = new JSONObject();
                jsonProva.put("provaId", rs.getInt("provaId"));
                jsonProva.put("descricao", rs.getString("descricao"));
                jsonProva.put("observacao", rs.getString("observacao"));
                
                java.sql.Timestamp dataAplicacaoStamp = rs.getTimestamp("dataAplicacao");
                java.sql.Date dataAplicacao = new Date( dataAplicacaoStamp.getTime());
                
                java.sql.Timestamp dataAtualStamp = rs.getTimestamp("dataAtual");
                java.sql.Date dataAtual = new Date( dataAtualStamp.getTime());
                
                if(dataAtual.after(dataAplicacao)){
                    jsonProva.put("podeEditar", false);
                }else{
                    jsonProva.put("podeEditar", true);
                }
                
                resolucaoProva = resolucaoProvaDAO.verificaResolucao(rs.getInt("provaId"), usuarioId);
                Boolean resolvida = false;
                if(resolucaoProva.getId() > 0){
                    resolvida = true;
                    jsonProva.put("notaFinal", resolucaoProva.getNotaFinal());
                }
                jsonProva.put("resolvida", resolvida);
                arrayProvas.add(jsonProva);
            }
            return arrayProvas;

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
    
    public void deleteProva(int id) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        try {
            stmt2 = con.prepareStatement(stmtDeleteDisciplinaProva);
            stmt2.setInt(1, id);
            stmt2.executeUpdate();
            
            stmt = con.prepareStatement(stmtDelete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Prova getById(int id) throws ClassNotFoundException {
        Prova prova = new Prova();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                prova.setId(rs.getInt("id"));
                prova.setDescricao(rs.getString("descricao"));
                prova.setObservacao(rs.getString("observacao"));
                
                java.sql.Timestamp dataAplicacaoStamp = rs.getTimestamp("dataAplicacao");
                java.util.Date dataAplicacao = new Date( dataAplicacaoStamp.getTime());
                prova.setDataAplicacao((Date) dataAplicacao);
                
                
                
            }
            return prova;
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
    
    public void insert(Prova prova, int disciplinaId){
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        int idObjeto = 0;
        try {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.ENGLISH);
            java.sql.Timestamp dataAplicacao = new java.sql.Timestamp(prova.getDataAplicacao().getTime());
            
            stmt = con.prepareStatement(stmtInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, prova.getDescricao());
            stmt.setString(2, prova.getObservacao());
            stmt.setTimestamp(3, dataAplicacao);
            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
            }

            prova.setId(idObjeto);
            
            stmt2 = con.prepareStatement(stmtInsertDisciplinaProva, Statement.RETURN_GENERATED_KEYS);
            stmt2.setInt(1, disciplinaId);
            stmt2.setInt(2, prova.getId());
            stmt2.execute();
            //Seta o id 
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
    
    public void update(Prova prova) {
        PreparedStatement stmt = null;
        try {
            if(prova.getId() > 0 ){
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.ENGLISH);
                java.sql.Timestamp dataAplicacao = new java.sql.Timestamp(prova.getDataAplicacao().getTime());

                
                stmt = con.prepareStatement(stmtUpdate);
                stmt.setString(1, prova.getDescricao());
                stmt.setString(2, prova.getObservacao());
                stmt.setTimestamp(3, dataAplicacao);
                stmt.setInt(4, prova.getId());
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
