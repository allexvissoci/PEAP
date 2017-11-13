/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Models.Materia;
import ModelsDAO.MateriaDAO;
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

/**
 *
 * @author allex
 */
@WebServlet(name = "ProcessaMateria", urlPatterns = {"/ProcessaMateria"})
public class ProcessaMateria extends HttpServlet {

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
            
            Materia materia = new Materia();
            MateriaDAO materiaDAO = new MateriaDAO();
            
            if(function == null){

                List<Materia> listaMateria = new ArrayList();
                try {
                    listaMateria = materiaDAO.getLista();

                    request.setAttribute("listaMateria", listaMateria);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/pages/materia.jsp");
                    rd.forward(request, response);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            
            if(function.equals("delete")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    materiaDAO.deleteMateria(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaCurso.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }

            if(function.equals("getDados")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    materia = materiaDAO.getById(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Gson gson = new Gson();
                String jsonMateria = gson.toJson(materia);
                
                out.println(jsonMateria);
                
            }
            
            if(function.equals("insert")){
                int id = Integer.parseInt((request.getParameter("id")));
                int disciplinaId = Integer.parseInt((request.getParameter("disciplinaId")));
                String nome = request.getParameter("nome");

                if(id > 0){
                    try {
                        materia = materiaDAO.getById(id);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                materia.setId(id);
                materia.setNome(nome);
                
                if(materia.getId() != 0){
                    //update
                    try{
                        materiaDAO.update(materia);
                        out.println(true);
                    } catch (Exception ex){
                        out.println(false);
                    }
                }else{
                    //insert
                    try{
                        materiaDAO.insert(materia, disciplinaId);
                        out.println(materia.getId());
                    } catch (Exception ex){
                        out.println(false);
                    }
                }

            }            
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
