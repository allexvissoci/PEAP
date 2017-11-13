/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Models.Alternativa;
import Models.Questao;
import ModelsDAO.AlternativaDAO;
import ModelsDAO.QuestaoDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author allex
 */
@WebServlet(name = "ProcessaAlternativa", urlPatterns = {"/ProcessaAlternativa"})
public class ProcessaAlternativa extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            String function = request.getParameter("function");
            
            Alternativa alternativa = new Alternativa();
            AlternativaDAO alternativaDAO = new AlternativaDAO();
            
            
            if(function.equals("delete")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    alternativaDAO.deleteAlternativa(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaCurso.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }

            if(function.equals("getDadosByQuestao")){
                int idQuestao = Integer.parseInt((request.getParameter("idQuestao")));
                Questao questao = new Questao();
                QuestaoDAO questaoDAO = new QuestaoDAO();
                questao = questaoDAO.getById(idQuestao);
                JSONArray arrayAlternativas = new JSONArray();
                try {
                    arrayAlternativas = alternativaDAO.getAlternativasByQuestao(questao.getId());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ProcessaAlternativa.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                out.println(arrayAlternativas);
                
            }
            
            if(function.equals("getDados")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    alternativa = alternativaDAO.getById(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Gson gson = new Gson();
                String jsonAlternativa = gson.toJson(alternativa);
                
                out.println(jsonAlternativa);
                
            }
            
            
            if(function.equals("insert")){
                int idQuestao = Integer.parseInt((request.getParameter("idQuestao")));
                String alternativasString = request.getParameter("alternativasArray");
                JSONParser parser = new JSONParser();
                JSONArray arrayAlternativas = (JSONArray)parser.parse(alternativasString);
                
                //// deleta todas as alternativas da questão
                Questao questao = new Questao();
                QuestaoDAO questaoDAO = new QuestaoDAO();
                questao = questaoDAO.getById(idQuestao);
                alternativaDAO.deleteByQuestao(questao.getId());
                
                
                for(Object objAlternativa : arrayAlternativas){
                    alternativa = new Alternativa();
                    JSONObject jsonAlternativaObject = (JSONObject) objAlternativa;
                    String descricao = (String) jsonAlternativaObject.get("descricao");
                    Boolean correta = (Boolean) jsonAlternativaObject.get("correta");
                    alternativa.setDescricao(descricao);
                    alternativa.setCorreta(correta);
                    alternativa.setQuestao(questao);
                    try{
                        alternativaDAO.insert(alternativa);
                    } catch (Exception ex){
                        out.println(false);
                    }
                }
            }            
        } catch (ParseException ex) {
            Logger.getLogger(ProcessaAlternativa.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProcessaAlternativa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
