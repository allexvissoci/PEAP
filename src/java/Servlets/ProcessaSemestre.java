/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Models.Semestre;
import ModelsDAO.SemestreDAO;
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
@WebServlet(name = "ProcessaSemestre", urlPatterns = {"/ProcessaSemestre"})
public class ProcessaSemestre extends HttpServlet {

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
            
            Semestre semestre = new Semestre();
            SemestreDAO semestreDAO = new SemestreDAO();
            
            if(function == null){

                List<Semestre> listaSemestre = new ArrayList();
                try {
                    listaSemestre = semestreDAO.getLista();

                    request.setAttribute("listaSemestre", listaSemestre);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/pages/semestre.jsp");
                    rd.forward(request, response);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(function.equals("getToSelect")){
                int cursoId = Integer.parseInt((request.getParameter("cursoId")));
                List<Semestre> listaSemestres = semestreDAO.getListaToSelect(cursoId);
                JSONArray jsonarray = new JSONArray();
                for(Semestre tur : listaSemestres){
                    JSONObject object = new JSONObject();
                    object.put("id", tur.getId());
                    object.put("nome", tur.getSemestreNome());
                    jsonarray.add(object);
                }
                out.println(jsonarray);
            }
            
            if(function.equals("delete")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    semestreDAO.deleteSemestre(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaCurso.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }

            if(function.equals("getDados")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    semestre = semestreDAO.getById(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Gson gson = new Gson();
                String jsonSemestre = gson.toJson(semestre);
                
                out.println(jsonSemestre);
                
            }
            
            if(function.equals("insert")){
                int id = Integer.parseInt((request.getParameter("id")));
                String nome = request.getParameter("nome");
                

                if(id > 0){
                    try {
                        semestre = semestreDAO.getById(id);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                semestre.setId(id);
                semestre.setSemestreNome(nome);

                if(semestre.getId() != 0){
                    //update
                    try{
                        semestreDAO.update(semestre);
                        out.println(true);
                    } catch (Exception ex){
                        out.println(false);
                    }
                }else{
                    //insert
                    try{
                        semestreDAO.insert(semestre);
                        out.println(semestre.getId());
                    } catch (Exception ex){
                        out.println(false);
                    }
                }

            }            
        } catch (SQLException ex) {
            Logger.getLogger(ProcessaSemestre.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProcessaSemestre.class.getName()).log(Level.SEVERE, null, ex);
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
