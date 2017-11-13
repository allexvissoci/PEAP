/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Models.Curso;
import Models.Disciplina;
import Models.MatriculaCurso;
import Models.MatriculaDisciplina;
import Models.Usuario;
import ModelsDAO.CursoDAO;
import ModelsDAO.DisciplinaDAO;
import ModelsDAO.MatriculaCursoDAO;
import ModelsDAO.MatriculaDisciplinaDAO;
import ModelsDAO.UsuarioDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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
@WebServlet(name = "ProcessaMatricula", urlPatterns = {"/ProcessaMatricula"})
public class ProcessaMatricula extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            
            String function = request.getParameter("function");
            
            MatriculaCurso matriculaCurso = new MatriculaCurso();
            MatriculaCursoDAO matriculaCursoDAO = new MatriculaCursoDAO();
            
            if(function.equals("curso")){
                
                List<MatriculaCurso> listMatriculaCurso = matriculaCursoDAO.getMatriculasCursos();
                
                request.setAttribute("matriculasCursos",listMatriculaCurso);
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/pages/matriculaCurso.jsp");
                rd.forward(request, response);
            }
            
            if(function.equals("getDadosMatriculaCurso")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    matriculaCurso = matriculaCursoDAO.getById(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Gson gson = new Gson();
                String jsonMatriculaCurso = gson.toJson(matriculaCurso);
                
                out.println(jsonMatriculaCurso);
                
            }
            
            if(function.equals("deleteMatriculaCurso")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    matriculaCursoDAO.deleteMatriculaCurso(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaCurso.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
            if(function.equals("insertMatriculaCurso")){
                int id = Integer.parseInt((request.getParameter("id")));
                int usuarioId = Integer.parseInt((request.getParameter("usuarioId")));
                int cursoId = Integer.parseInt((request.getParameter("cursoId")));
                
                if(id > 0){
                    try {
                        matriculaCurso = matriculaCursoDAO.getById(id);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                Usuario usuario = new Usuario();
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                usuario = usuarioDAO.getById(usuarioId);
                matriculaCurso.setUsuario(usuario);
                
                Curso curso = new Curso();
                CursoDAO cursoDAO = new CursoDAO();
                curso = cursoDAO.getById(cursoId);
                matriculaCurso.setCurso(curso);
                
                if(matriculaCurso.getId() != 0){
                    //update
                    try{
                        matriculaCursoDAO.update(matriculaCurso);
                        out.println(true);
                    } catch (Exception ex){
                        out.println(false);
                    }
                }else{
                    //insert
                    try{
                        matriculaCursoDAO.insertMatriculaCurso(matriculaCurso);
                        out.println(matriculaCurso.getId());
                    } catch (Exception ex){
                        out.println(false);
                    }
                }
                
            }
            /////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////
            //////////////////////////////DISCIPLINAS ///////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////
            
            MatriculaDisciplina matriculaDisciplina = new MatriculaDisciplina();
            MatriculaDisciplinaDAO matriculaDisciplinaDAO = new MatriculaDisciplinaDAO();
            
            if(function.equals("disciplina")){
                List<MatriculaDisciplina> listMatriculaDisciplina = matriculaDisciplinaDAO.getMatriculasDisciplinas();
                
                request.setAttribute("matriculasDisciplinas",listMatriculaDisciplina);
                
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/pages/matriculaDisciplina.jsp");
                rd.forward(request, response);
            }
            
            if(function.equals("getDadosMatriculaDisciplina")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    matriculaDisciplina = matriculaDisciplinaDAO.getById(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Gson gson = new Gson();
                String jsonMatriculaDisciplina = gson.toJson(matriculaDisciplina);
                
                out.println(jsonMatriculaDisciplina);
                
            }
            
            if(function.equals("deleteMatriculaDisciplina")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    matriculaDisciplinaDAO.deleteMatriculaDisciplina(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaMatricula.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
            if(function.equals("insertMatriculaDisciplina")){
                int id = Integer.parseInt((request.getParameter("id")));
                int usuarioId = Integer.parseInt((request.getParameter("usuarioId")));
                int disciplinaId = Integer.parseInt((request.getParameter("disciplinaId")));
                
                if(id > 0){
                    try {
                        matriculaDisciplina = matriculaDisciplinaDAO.getById(id);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                Usuario usuario = new Usuario();
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                usuario = usuarioDAO.getById(usuarioId);
                matriculaDisciplina.setUsuario(usuario);
                
                Disciplina disciplina = new Disciplina();
                DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
                disciplina = disciplinaDAO.getById(disciplinaId);
                matriculaDisciplina.setDisciplina(disciplina);
                
                if(matriculaDisciplina.getId() != 0){
                    //update
                    try{
                        matriculaDisciplinaDAO.update(matriculaDisciplina);
                        out.println(true);
                    } catch (Exception ex){
                        out.println(false);
                    }
                }else{
                    //insert
                    try{
                        matriculaDisciplinaDAO.insertMatriculaDisciplina(matriculaDisciplina);
                        out.println(matriculaDisciplina.getId());
                    } catch (Exception ex){
                        out.println(false);
                    }
                }
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProcessaMatricula.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProcessaMatricula.class.getName()).log(Level.SEVERE, null, ex);
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
