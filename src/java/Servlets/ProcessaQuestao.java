/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Models.NivelDificuldade;
import Models.Questao;
import Models.ResolucaoProva;
import Models.ResolucaoQuestao;
import ModelsDAO.NivelDificuldadeDAO;
import ModelsDAO.QuestaoDAO;
import ModelsDAO.ResolucaoProvaDAO;
import ModelsDAO.ResolucaoQuestaoDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author allex
 */
@WebServlet(name = "ProcessaQuestao", urlPatterns = {"/ProcessaQuestao"})
public class ProcessaQuestao extends HttpServlet {

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
            
            Questao questao = new Questao();
            QuestaoDAO questaoDAO = new QuestaoDAO();
            
            
            if(function.equals("delete")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    questaoDAO.deleteQuestao(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaCurso.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
            if(function.equals("anula")){
                int idQuestao = Integer.parseInt((request.getParameter("idQuestao")));
                int idProva = Integer.parseInt((request.getParameter("idProva")));
                Boolean anular = Boolean.parseBoolean((request.getParameter("anular")));
                
                questaoDAO.anulaQuestao(idQuestao, anular);
                
                ////recalcula nota
                //// recupera a resolucao da prova
                List<ResolucaoProva> lstResProv = new ArrayList();
                ResolucaoProvaDAO resProvaDAO = new ResolucaoProvaDAO();
                ResolucaoQuestaoDAO resQuestaoDAO = new ResolucaoQuestaoDAO();
                lstResProv = resProvaDAO.getResolucaoProvaByProvaId(idProva);
                //// recupera todas as resolucoes de questoes
                for(ResolucaoProva resProv : lstResProv){
                    List<ResolucaoQuestao> lstResQuestao = new ArrayList();
                    lstResQuestao = resQuestaoDAO.getResolucaoQuestaoByResProva(resProv.getId());
                    float nota = 0;
                    for(ResolucaoQuestao resQuest : lstResQuestao){
                        if(resQuest.getAnulada() || resQuest.getAlternativaSelecionada() == resQuest.getAlternativaCorreta()){
                            nota = nota + resQuest.getQuestao().getValor();
                        }
                    }
                    resProv.setNotaFinal(nota);
                    resProvaDAO.update(resProv);
                }
                //// compara alternativas soma nota
                //// atualiza nota
                
            }

            if(function.equals("getDados")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    questao = questaoDAO.getById(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Gson gson = new Gson();
                String jsonQuestao = gson.toJson(questao);
                
                out.println(jsonQuestao);
                
            }
                      
        
            if(function.equals("insert")){
                int id = Integer.parseInt((request.getParameter("id")));
                int idProva = Integer.parseInt((request.getParameter("idProva")));
                int nivelDificuldadeId = Integer.parseInt((request.getParameter("nivelDificuldadeId")));
                String enunciado = request.getParameter("enunciado");
                int valor = Integer.parseInt((request.getParameter("valor")));

                if(id > 0){
                    try {
                        questao = questaoDAO.getById(id);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                questao.setId(id);
                questao.setEnunciado(enunciado);
                questao.setValor(valor);
                NivelDificuldade nivel = new NivelDificuldade();
                NivelDificuldadeDAO nivelDAO = new NivelDificuldadeDAO();
                nivel = nivelDAO.getById(nivelDificuldadeId);
                questao.setNivelDificuldade(nivel);

                if(questao.getId() != 0){
                    //update
                    try{
                        questaoDAO.update(questao);
                        out.println(true);
                    } catch (Exception ex){
                        out.println(false);
                    }
                }else{
                    //insert
                    try{
                        questaoDAO.insert(questao, idProva);
                        out.println(questao.getId());
                    } catch (Exception ex){
                        out.println(false);
                    }
                }

            }    
            
            
            if(function.equals("getNivelDificuldadeToSelect")){
                NivelDificuldadeDAO nivelDAO = new NivelDificuldadeDAO();
                
                List<NivelDificuldade> listaNivel = nivelDAO.getListaToSelect();
                JSONArray jsonarray = new JSONArray();
                for(NivelDificuldade nivel : listaNivel){
                    JSONObject object = new JSONObject();
                    object.put("id", nivel.getId());
                    object.put("descricao", nivel.getDescricao());
                    jsonarray.add(object);
                }
                out.println(jsonarray);
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProcessaQuestao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProcessaQuestao.class.getName()).log(Level.SEVERE, null, ex);
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
