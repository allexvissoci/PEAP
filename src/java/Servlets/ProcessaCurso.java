/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Models.Curso;
import Models.Usuario;
import ModelsDAO.CursoDAO;
import ModelsDAO.UsuarioDAO;
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
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author allex
 */
@WebServlet(name = "ProcessaCurso", urlPatterns = {"/ProcessaCurso"})
public class ProcessaCurso extends HttpServlet {

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
            
            Curso curso = new Curso();
            CursoDAO cursoDAO = new CursoDAO();
           
            
            if(function == null){
                List<Curso> listaCursos = new ArrayList();
                try {
                    ////recupera lista de cursos
                    HttpSession sessionusu = request.getSession();
                    Usuario usuario =  (Usuario) sessionusu.getAttribute("Usuario");
                    listaCursos = cursoDAO.getLista(usuario);
                    
                    //// cria um JSONArray com todos os cursos, mostrando as turmas pertencentes
                    JSONArray jsonCursosArray = new JSONArray();
                    for(Curso cur : listaCursos){
                        
                        JSONArray jsonTurmasArray = new JSONArray();
                        JSONObject JsonCursoObject = new JSONObject();
                        JsonCursoObject.put("cursoNome", cur.getNome());
                        JsonCursoObject.put("cursoId", cur.getId());
                        
                        //// recupera todas as turmas do curso
                        JSONArray jsonTurmasArrayAUX = cursoDAO.getTurmas(cur.getId());
                        
                        //// cria um JsonArray para popular o JsonArray principal (o de curso)
//                        ArrayList listSemestreAux = new ArrayList();;
                        ArrayList listSemestreAux = new ArrayList();
                        for(Object turmasObj : jsonTurmasArrayAUX) {
                            JSONArray jsonTurnosArray = new JSONArray();
                            JSONObject jsonTurmaObject = (JSONObject) turmasObj;
                            int semestreId = (int) jsonTurmaObject.get("semestreId");
                            int turmaId = (int) jsonTurmaObject.get("turmaId");
                            //// verifica se o semestre já foi incluido no JSONObject
                            if(!listSemestreAux.contains(semestreId)){
                                listSemestreAux.add(semestreId);
                                JSONObject jsonSemestreObject = new JSONObject();
                                
                                jsonSemestreObject.put("turmaId", turmaId);
                                jsonSemestreObject.put("semestreId", semestreId);
                                jsonSemestreObject.put("semestreNome", jsonTurmaObject.get("turmaNome")+" "+jsonTurmaObject.get("semestreNome"));
                                
                                //// recupera os turnos dos cursos de acordo com o semestre
                                JSONArray jsonTurnosArrayAUX = cursoDAO.getTurnos(cur.getId());
                                //// cria um JsonArray para popular o JsonArray de turma
                                for(Object turnosObj : jsonTurnosArrayAUX) {
                                    JSONObject jsonTunoObjectAUX = (JSONObject) turnosObj;
                                    JSONObject jsonTunoObject = new JSONObject();
                                    
                                    int newSemestreId = (int) jsonTunoObjectAUX.get("semestreId");
                                    if(semestreId == newSemestreId){
                                        
                                        int newTurmaId = (int) jsonTunoObjectAUX.get("turmaId");
                                        
                                        jsonTunoObject.put("turnoId", jsonTunoObjectAUX.get("turnoId"));
                                        jsonTunoObject.put("turnoNome", jsonTunoObjectAUX.get("turnoNome"));
                                        jsonTunoObject.put("turnoNome", jsonTunoObjectAUX.get("turnoNome"));
                                        jsonTunoObject.put("newTurmaId", newTurmaId);

                                        JSONArray jsonPeriodosArray = new JSONArray();
                                        JSONArray jsonPeriodosAUX = cursoDAO.getPeriodos(newTurmaId, (int) jsonTunoObjectAUX.get("turnoId"));
                                        for(Object periodoObj : jsonPeriodosAUX) {
                                            JSONObject jsonPeriodoObjectAUX = (JSONObject) periodoObj;
                                            JSONObject jsonPeriodoObject = new JSONObject();
                                            jsonPeriodoObject.put("periodoId", jsonPeriodoObjectAUX.get("periodoId"));
                                            jsonPeriodoObject.put("periodoNome", jsonPeriodoObjectAUX.get("periodoNome"));
                                            
                                            JSONArray jsonDisciplinasAUX = cursoDAO.getDisciplinas(cur.getId(), (int) jsonPeriodoObjectAUX.get("periodoId"), turmaId);
                                            jsonPeriodoObject.put("disciplinas", jsonDisciplinasAUX);

                                            jsonPeriodosArray.add(jsonPeriodoObject);
                                        }
                                        jsonTunoObject.put("periodos", jsonPeriodosArray);
                                        jsonTurnosArray.add(jsonTunoObject);
                                    }
                                }
                                jsonSemestreObject.put("turnos", jsonTurnosArray);
                                jsonTurmasArray.add(jsonSemestreObject);
                            }
                            
                        }
                        JsonCursoObject.put("turmas", jsonTurmasArray);                    
                        jsonCursosArray.add(JsonCursoObject);
                    }
                    
                    request.setAttribute("listaCursos", jsonCursosArray);;
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/pages/cursos.jsp");
                    rd.forward(request, response);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(ProcessaCurso.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaCurso.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(function.equals("getTurmasByCurso")){
                int cursoId = Integer.parseInt((request.getParameter("cursoId")));
                JSONArray jsonTurmasArrayAUX = cursoDAO.getTurmas(cursoId);
                out.println(jsonTurmasArrayAUX);
            }
            
            if(function.equals("toSelect")){
                HttpSession sessionusu = request.getSession();
                Usuario usuario =  (Usuario) sessionusu.getAttribute("Usuario");
                int usuarioSelectedId = Integer.parseInt((request.getParameter("usuarioId")));
                List<Curso> listaCursos = cursoDAO.getListaToSelect(usuario, usuarioSelectedId);
                JSONArray jsonarray = new JSONArray();
                for(Curso cur : listaCursos){
                    JSONObject object = new JSONObject();
                    object.put("id", cur.getId());
                    object.put("nome", cur.getNome());
                    jsonarray.add(object);
                }
                out.println(jsonarray);
            }
            
            if(function.equals("getCursosByUsuariotoSelect")){
                
                int usuarioSelectedId = Integer.parseInt((request.getParameter("usuarioId")));
                Usuario usuario = new Usuario();
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                usuario = usuarioDAO.getById(usuarioSelectedId);
                List<Curso> listaCursos = cursoDAO.getListaToSelect(usuario, 0);
                JSONArray jsonarray = new JSONArray();
                for(Curso cur : listaCursos){
                    JSONObject object = new JSONObject();
                    object.put("id", cur.getId());
                    object.put("nome", cur.getNome());
                    jsonarray.add(object);
                }
                out.println(jsonarray);
            }
            
            if(function.equals("getDados")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    curso = cursoDAO.getById(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Gson gson = new Gson();
                String jsonUsuario = gson.toJson(curso);
                
                out.println(jsonUsuario);
                
            }            
            
            if(function.equals("delete")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    cursoDAO.deleteCurso(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaCurso.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
            if(function.equals("insert")){
                int id = Integer.parseInt((request.getParameter("id")));
                String nome = request.getParameter("nome");
                

                if(id > 0){
                    try {
                        curso = cursoDAO.getById(id);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                curso.setId(id);
                curso.setNome(nome);


                if(curso.getId() != 0){
                    ////update
                    try{
                        cursoDAO.update(curso);
                        out.println(true);
                    } catch (Exception ex){
                        out.println(false);
                    };
                }else{
                    ////insert
                    try{
                        cursoDAO.insert(curso);
                        out.println(curso.getId());
                    } catch (Exception ex){
                        out.println(false);
                    }
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(ProcessaCurso.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProcessaCurso.class.getName()).log(Level.SEVERE, null, ex);
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
