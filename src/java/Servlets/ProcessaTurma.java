/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Models.Curso;
import Models.Semestre;
import Models.Turma;
import Models.Turno;
import ModelsDAO.CursoDAO;
import ModelsDAO.SemestreDAO;
import ModelsDAO.TurmaDAO;
import ModelsDAO.TurnoDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author allex
 */
@WebServlet(name = "ProcessaTurma", urlPatterns = {"/ProcessaTurma"})
public class ProcessaTurma extends HttpServlet {

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
            
            Turma turma = new Turma();
            TurmaDAO turmaDAO = new TurmaDAO();
            
           
            
            if(function.equals("delete")){
                int id = Integer.parseInt((request.getParameter("turmaId")));
                try {
                    turmaDAO.deleteTurma(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaCurso.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }

            if(function.equals("getDados")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    turma = turmaDAO.getById(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Gson gson = new Gson();
                String jsonTurma = gson.toJson(turma);
                
                out.println(jsonTurma);
                
            }
            
            if(function.equals("insert")){
                int id = Integer.parseInt((request.getParameter("id")));
                String nome = request.getParameter("nome");
                int idCurso = Integer.parseInt((request.getParameter("idCurso")));
                int turnoId = Integer.parseInt((request.getParameter("turnoId")));
                int semestreId = Integer.parseInt((request.getParameter("semestreId")));
                

                if(id > 0){
                    try {
                        turma = turmaDAO.getById(id);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                turma.setId(id);
                turma.setNome(nome);
                Curso curso = new Curso();
                CursoDAO cursoDAO = new CursoDAO();
                curso = cursoDAO.getById(idCurso);
                turma.setCurso(curso);
                
                Turno turno = new Turno();
                TurnoDAO turnoDAO = new TurnoDAO();
                turno = turnoDAO.getById(turnoId);
                turma.setTurno(turno);
                
                Semestre semestre = new Semestre();
                SemestreDAO semestreDAO = new SemestreDAO();
                semestre = semestreDAO.getById(semestreId);
                turma.setSemestre(semestre);
                
                if(turma.getId() != 0){
                    //update
                    try{
                        turmaDAO.update(turma);
                        out.println(true);
                    } catch (Exception ex){
                        out.println(false);
                    }
                }else{
                    //insert
                    try{
                        turmaDAO.insert(turma);
                        out.println(turma.getId());
                    } catch (Exception ex){
                        out.println(false);
                    }
                }

            }            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProcessaTurma.class.getName()).log(Level.SEVERE, null, ex);
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
