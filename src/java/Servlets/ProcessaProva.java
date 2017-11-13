/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Models.Alternativa;
import Models.Prova;
import Models.Questao;
import Models.ResolucaoProva;
import Models.ResolucaoQuestao;
import Models.Usuario;
import ModelsDAO.AlternativaDAO;
import ModelsDAO.ProvaDAO;
import ModelsDAO.QuestaoDAO;
import ModelsDAO.ResolucaoProvaDAO;
import ModelsDAO.ResolucaoQuestaoDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author allex
 */
@WebServlet(name = "ProcessaProva", urlPatterns = {"/ProcessaProva"})
public class ProcessaProva extends HttpServlet {

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
            
            Prova prova = new Prova();
            ProvaDAO provaDAO = new ProvaDAO();
            
            if(function.equals("provaDetail")){
                int id = Integer.parseInt((request.getParameter("id")));
                Boolean resolvida = Boolean.parseBoolean((request.getParameter("resolvida")));
                
                
                prova = provaDAO.getById(id);
                ////pegar questões desta prova
                List<Questao> listQuestao = new ArrayList();
                QuestaoDAO questaoDAO = new QuestaoDAO();
                listQuestao = questaoDAO.getQuestoesByDisciplinaId(prova.getId());
                
                JSONArray jsonQuestaoArray = new JSONArray();
                Alternativa alternativa = new Alternativa();
                AlternativaDAO alternativaDAO = new AlternativaDAO();
                for(Questao quest: listQuestao){
                    JSONObject jsonQuestaoObject = new JSONObject();
                    jsonQuestaoObject.put("questaoId", quest.getId());
                    jsonQuestaoObject.put("enunciado", quest.getEnunciado());
                    jsonQuestaoObject.put("valor", quest.getValor());
                    jsonQuestaoObject.put("anulada", quest.getAnulada());
                    jsonQuestaoObject.put("nivelDificuldade", quest.getNivelDificuldade().getDescricao());
                    
                    JSONArray jsonAlternativasArrayAUX = alternativaDAO.getAlternativasByQuestao(quest.getId());
                    JSONArray jsonAlternativasArray = new JSONArray();
                    //// cria um JsonArray para popular o JsonArray de turma
                    for(Object alternativaObj : jsonAlternativasArrayAUX) {
                        JSONObject jsonAlternativaObjectAUX = (JSONObject) alternativaObj;
                        JSONObject jsonAlternativaObject = new JSONObject();
                        
                        jsonAlternativaObject.put("alternativaId", jsonAlternativaObjectAUX.get("alternativaId"));
                        jsonAlternativaObject.put("alternativaDescricao", jsonAlternativaObjectAUX.get("descricao"));
                        jsonAlternativaObject.put("correta", jsonAlternativaObjectAUX.get("correta"));
                        
                        jsonAlternativasArray.add(jsonAlternativaObject);
                    }
                    jsonQuestaoObject.put("alternativas", jsonAlternativasArray);
                    jsonQuestaoArray.add(jsonQuestaoObject);
                }
                Boolean podeEditar = Boolean.parseBoolean((request.getParameter("podeEditar")));
                request.setAttribute("podeEditar", podeEditar);
                request.setAttribute("resolvida", resolvida);
                request.setAttribute("prova", prova);
                request.setAttribute("questoes", jsonQuestaoArray);
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/pages/provaDetail.jsp");
                rd.forward(request, response);
            }
            
            if(function.equals("correcao")){
                
                int idProva = Integer.parseInt((request.getParameter("idProva")));
                HttpSession sessionusu = request.getSession();
                Usuario usuario = (Usuario) sessionusu.getAttribute("Usuario");
                
                ////verifica se prova já foi resolvida pelo usuário
                ResolucaoProva resolucaoProva = new ResolucaoProva();
                ResolucaoProvaDAO resolucaoProvaDAO = new ResolucaoProvaDAO();
                resolucaoProva = resolucaoProvaDAO.verificaResolucao(idProva, usuario.getId());
                Boolean resolvida = false;
                if(resolucaoProva.getId() > 0){
                    resolvida = true;
                }
                if(!resolvida){
                    String respostasString = request.getParameter("arrayRespostas");
                    JSONParser parser = new JSONParser();
                    JSONArray arrayRespostas = (JSONArray)parser.parse(respostasString);

                    ResolucaoProva resProva = new ResolucaoProva();
                    ResolucaoProvaDAO resProvaDAO = new ResolucaoProvaDAO();
                    prova = provaDAO.getById(idProva);

                    resProva.setProva(prova);
                    resProva.setUsuario(usuario);
                    resProvaDAO.insert(resProva);

                    float nota = 0;
                    for(Object objResposta : arrayRespostas){
                        JSONObject jsonRespostaObject = (JSONObject) objResposta;
                        int idQuestao = Integer.parseInt((String) jsonRespostaObject.get("idQuestao"));
                        int idAlternativa = Integer.parseInt((String) jsonRespostaObject.get("idAlternativa"));
                        boolean anulada = Boolean.parseBoolean((String) jsonRespostaObject.get("anulada"));

                        ////verifica se a resposta é correta
                        QuestaoDAO questaoDAO = new QuestaoDAO();
                        JSONObject jsonReturnResposta = questaoDAO.checkAlternativa(idAlternativa);

                        if(jsonReturnResposta.size() > 0 || anulada){
                            float valor = (float) jsonReturnResposta.get("valor");
                            nota = nota + valor;
                        }
                        ResolucaoQuestao resQuestao = new ResolucaoQuestao();
                        ResolucaoQuestaoDAO resQuestaoDAO = new ResolucaoQuestaoDAO();

                        resQuestao.setResolucaoProva(resProva);
                        Questao questao = new Questao();
                        questao = questaoDAO.getById(idQuestao);
                        resQuestao.setQuestao(questao);

                        AlternativaDAO alternativaDAO = new AlternativaDAO();
                        Alternativa alternativaSelecionada = alternativaDAO.getById(idAlternativa);
                        resQuestao.setAlternativaSelecionada(alternativaSelecionada);

                        Alternativa alternativaCorreta = alternativaDAO.getAlternativaCorretaByQuestao(questao.getId());
                        resQuestao.setAlternativaCorreta(alternativaCorreta);
                        resQuestaoDAO.insert(resQuestao);
                    }

                    resProva.setNotaFinal(nota);
                    resProvaDAO.update(resProva);
                    out.println(nota);
                }else{
                    out.println(resolvida);
                }
            }
            
            if(function.equals("relatorioProva")){
                int idProva = Integer.parseInt((request.getParameter("id")));
                ResolucaoQuestaoDAO resolucaoQuestaoDAO = new ResolucaoQuestaoDAO();
                List<ResolucaoProva> lstRelatorio = new ArrayList();
                lstRelatorio = resolucaoQuestaoDAO.getRelatorioProva(idProva);
                
                request.setAttribute("lstRelatorio", lstRelatorio);
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/pages/relatorioProva.jsp");
                rd.forward(request, response);
                
            }
            
            if(function.equals("getResolucao")){
                int idProva = Integer.parseInt((request.getParameter("idProva")));
                int idQuestao = Integer.parseInt((request.getParameter("idQuestao")));
                
                ResolucaoQuestao resolucaoQuestao = new ResolucaoQuestao();
                ResolucaoQuestaoDAO resolucaoQuestaoDAO = new ResolucaoQuestaoDAO();
                HttpSession sessionusu = request.getSession();
                int idUsuario =  (int) sessionusu.getAttribute("userid");
                resolucaoQuestao = resolucaoQuestaoDAO.getResolucaoByProvaAndQuestao(idProva, idQuestao, idUsuario);
                
                Gson gson = new Gson();
                String jsonResolucaoQuestao = gson.toJson(resolucaoQuestao);
                
                out.println(jsonResolucaoQuestao);
                
            }
            
            if(function.equals("delete")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    provaDAO.deleteProva(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaCurso.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }

            if(function.equals("getDados")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    prova = provaDAO.getById(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
                String jsonProva = gson.toJson(prova);
                
                out.println(jsonProva);
                
            }
            
            if(function.equals("insert")){
                int id = Integer.parseInt((request.getParameter("id")));
                int disciplinaId = Integer.parseInt((request.getParameter("disciplinaId")));
                String descricao = request.getParameter("descricao");
                String observacao = request.getParameter("observacao");
                String dataAplicacao = request.getParameter("dataAplicacao")+ " 23:59:59";
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.ENGLISH);
                java.sql.Date dataAplicacaoSql = new java.sql.Date(format.parse(dataAplicacao).getTime());
                
                if(id > 0){
                    try {
                        prova = provaDAO.getById(id);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                prova.setId(id);
                prova.setDescricao(descricao);
                prova.setObservacao(observacao);
                prova.setDataAplicacao(dataAplicacaoSql);

                if(prova.getId() != 0){
                    //update
                    try{
                        provaDAO.update(prova);
                        out.println(true);
                    } catch (Exception ex){
                        out.println(false);
                    }
                }else{
                    //insert
                    try{
                        provaDAO.insert(prova, disciplinaId);
                        out.println(prova.getId());
                    } catch (Exception ex){
                        out.println(false);
                    }
                }

            }            
         
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProcessaProva.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProcessaProva.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ProcessaProva.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.text.ParseException ex) {
            Logger.getLogger(ProcessaProva.class.getName()).log(Level.SEVERE, null, ex);
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
