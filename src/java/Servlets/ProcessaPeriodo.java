/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Models.Periodo;
import Models.Turma;
import Models.Turno;
import ModelsDAO.PeriodoDAO;
import ModelsDAO.TurmaDAO;
import ModelsDAO.TurnoDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
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
@WebServlet(name = "ProcessaPeriodo", urlPatterns = {"/ProcessaPeriodo"})
public class ProcessaPeriodo extends HttpServlet {

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
            
            Periodo periodo = new Periodo();
            PeriodoDAO periodoDAO = new PeriodoDAO();
            
            if(function == null){

                List<Periodo> listaPeriodo = new ArrayList();
                try {
                    listaPeriodo = periodoDAO.getLista();

                    request.setAttribute("listaPeriodo", listaPeriodo);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/pages/periodo.jsp");
                    rd.forward(request, response);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(function.equals("getToSelect")){
                int turmaId = Integer.parseInt((request.getParameter("turmaId")));
                List<Periodo> listaPeriodos = periodoDAO.getListaToSelect(turmaId);
                JSONArray jsonarray = new JSONArray();
                for(Periodo per : listaPeriodos){
                    JSONObject object = new JSONObject();
                    object.put("id", per.getId());
                    object.put("nome", per.getPeriodoNome());
                    jsonarray.add(object);
                }
                out.println(jsonarray);
            }
            
            if(function.equals("removerDeTurma")){
                int turmaId = Integer.parseInt((request.getParameter("turmaId")));
                int periodoId = Integer.parseInt((request.getParameter("periodoId")));
                
                Turma turma = new Turma();
                TurmaDAO turmaDAO = new TurmaDAO();
                turma = turmaDAO.getById(turmaId);
                
                periodo = periodoDAO.getById(periodoId);
                                
                periodoDAO.removeDeTurma(periodo,turma);
            }
            
            if(function.equals("addPeriodoEmTurma")){
                int turmaId = Integer.parseInt((request.getParameter("turmaId")));
                int periodoId = Integer.parseInt((request.getParameter("periodoId")));
                
                Turma turma = new Turma();
                TurmaDAO turmaDAO = new TurmaDAO();
                turma = turmaDAO.getById(turmaId);
                
                periodo = periodoDAO.getById(periodoId);
                
                try{
                    periodoDAO.addPeriodoEmTurma(periodo, turma);
                    out.println(true);
                } catch (Exception ex){
                    out.println(false);
                }
                
            }
            
            if(function.equals("delete")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    periodoDAO.deletePeriodo(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaCurso.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }

            if(function.equals("getDados")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    periodo = periodoDAO.getById(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Gson gson = new Gson();
                String jsonPeriodo = gson.toJson(periodo);
                
                out.println(jsonPeriodo);
                
            }
            
            
            if(function.equals("insert")){
                int id = Integer.parseInt((request.getParameter("id")));
                String nome = request.getParameter("nome");
                

                if(id > 0){
                    try {
                        periodo = periodoDAO.getById(id);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                periodo.setId(id);
                periodo.setPeriodoNome(nome);

                if(periodo.getId() != 0){
                    //update
                    try{
                        periodoDAO.update(periodo);
                        out.println(true);
                    } catch (Exception ex){
                        out.println(false);
                    }
                }else{
                    //insert
                    try{
                        periodoDAO.insert(periodo);
                        out.println(periodo.getId());
                    } catch (Exception ex){
                        out.println(false);
                    }
                }

            }            
        } catch (SQLException ex) {
            Logger.getLogger(ProcessaPeriodo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProcessaPeriodo.class.getName()).log(Level.SEVERE, null, ex);
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
