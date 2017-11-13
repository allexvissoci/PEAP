/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Models.ArquivosMateria;
import Models.Curso;
import Models.Disciplina;
import Models.Materia;
import Models.Periodo;
import Models.Turma;
import Models.Usuario;
import ModelsDAO.ArquivosMateriaDAO;
import ModelsDAO.CursoDAO;
import ModelsDAO.DisciplinaDAO;
import ModelsDAO.MateriaDAO;
import ModelsDAO.PeriodoDAO;
import ModelsDAO.ProvaDAO;
import ModelsDAO.TurmaDAO;
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
@WebServlet(name = "ProcessaDisciplina", urlPatterns = {"/ProcessaDisciplina"})
public class ProcessaDisciplina extends HttpServlet {

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
            
            Disciplina disciplina = new Disciplina();
            DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
            
            if(function == null){

                List<Disciplina> listaDisciplina = new ArrayList();
                try {
                    listaDisciplina = disciplinaDAO.getLista();

                    request.setAttribute("listaDisciplina", listaDisciplina);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/pages/disciplina.jsp");
                    rd.forward(request, response);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(function.equals("removerDePeriodo")){
                int turmaId = Integer.parseInt((request.getParameter("turmaId")));
                int periodoId = Integer.parseInt((request.getParameter("periodoId")));
                int disciplinaId = Integer.parseInt((request.getParameter("disciplinaId")));
                
                Turma turma = new Turma();
                TurmaDAO turmaDAO = new TurmaDAO();
                turma = turmaDAO.getById(turmaId);
                
                Periodo periodo = new Periodo();
                PeriodoDAO periodoDAO = new PeriodoDAO();
                periodo = periodoDAO.getById(periodoId);
                
                disciplina = disciplinaDAO.getById(disciplinaId);
                
                disciplinaDAO.removeDePeriodo(periodo,disciplina,turma);
            }
            
            if(function.equals("getToSelect")){
                int turmaId = Integer.parseInt((request.getParameter("turmaId")));
                int periodoId = Integer.parseInt((request.getParameter("periodoId")));
                int cursoId = Integer.parseInt((request.getParameter("cursoId")));
                List<Disciplina> listaDisciplinas = disciplinaDAO.getListaToAddEmPeriodoSelect(turmaId, periodoId, cursoId);
                JSONArray jsonarray = new JSONArray();
                for(Disciplina disc : listaDisciplinas){
                    JSONObject object = new JSONObject();
                    object.put("id", disc.getId());
                    object.put("nome", disc.getNome());
                    jsonarray.add(object);
                }
                out.println(jsonarray);
            }
            
            if(function.equals("addDisciplinaEmPeriodo")){
                int turmaId = Integer.parseInt((request.getParameter("turmaId")));
                int periodoId = Integer.parseInt((request.getParameter("periodoId")));
                int disciplinaId = Integer.parseInt((request.getParameter("disciplinaId")));
                
                Turma turma = new Turma();
                TurmaDAO turmaDAO = new TurmaDAO();
                turma = turmaDAO.getById(turmaId);
                
                Periodo periodo = new Periodo();
                PeriodoDAO periodoDAO = new PeriodoDAO();
                periodo = periodoDAO.getById(periodoId);
                
                disciplina = disciplinaDAO.getById(disciplinaId);
                
                try{
                    disciplinaDAO.addDisciplinaEmPeriodo(periodo,turma, disciplina);
                    out.println(true);
                } catch (Exception ex){
                    out.println(false);
                }
                
            }
            
            
            if(function.equals("toSelect")){
                int cursoId = Integer.parseInt((request.getParameter("cursoId")));
                int usuarioId = Integer.parseInt((request.getParameter("usuarioId")));
                
                List<Disciplina> listaDisciplinas = disciplinaDAO.getListaToSelect(cursoId, usuarioId);
                JSONArray jsonarray = new JSONArray();
                for(Disciplina dis : listaDisciplinas){
                    JSONObject object = new JSONObject();
                    object.put("id", dis.getId());
                    object.put("nome", dis.getNome());
                    jsonarray.add(object);
                }
                out.println(jsonarray);
            }
            
            if(function.equals("disciplinaDetail")){
                int id = Integer.parseInt((request.getParameter("id")));
                disciplina = disciplinaDAO.getById(id);
                ////pegar matérias desta disciplina
                List<Materia> listMaterias = new ArrayList();
                MateriaDAO materiaDAO = new MateriaDAO();
                listMaterias = materiaDAO.getMateriasByDisciplinaId(disciplina.getId());
                JSONArray materiasArray = new JSONArray();
                for(Materia mat : listMaterias){
                    JSONObject materiaObject = new JSONObject();
                    materiaObject.put("materiaId", mat.getId());
                    materiaObject.put("materiaNome", mat.getNome());
                    List<ArquivosMateria> lstArquivosMateria = new ArrayList();
                    ArquivosMateriaDAO arquivoDAO = new ArquivosMateriaDAO();
                    lstArquivosMateria = arquivoDAO.getArquivosByMateria(mat.getId());
                    JSONArray arquivosArray = new JSONArray();
                    for(ArquivosMateria arq : lstArquivosMateria){
                        JSONObject arquivoObject = new JSONObject();
                        arquivoObject.put("arquivoId", arq.getId());
                        arquivoObject.put("arquivoDescricao", arq.getDescricao());
                        arquivoObject.put("arquivoPath", arq.getPath());
                        arquivoObject.put("arquivoTipoArquivo", arq.getTipoArquivo());
                        arquivosArray.add(arquivoObject);
                    }
                    materiaObject.put("arquivos", arquivosArray);
                    materiasArray.add(materiaObject);
                }
                
                
                JSONArray arrayProvas = new JSONArray();
                ProvaDAO provaDAO = new ProvaDAO();
                
                HttpSession sessionusu = request.getSession();
                String tipoUsuario =  (String) sessionusu.getAttribute("tipoUsuario");
                Usuario usuario =  (Usuario) sessionusu.getAttribute("Usuario");
                arrayProvas = provaDAO.getProvasByDisciplinaId(disciplina.getId(), tipoUsuario, usuario.getId());
                JSONArray listProvas = new JSONArray();
                for(Object provaObj : arrayProvas) {
                    JSONObject jsonobjectProvaAUX = (JSONObject) provaObj;
                    JSONObject jsonobjectProva = new JSONObject();
                    int idProva = (int) jsonobjectProvaAUX.get("provaId");
                    Boolean resolvida = (Boolean) jsonobjectProvaAUX.get("resolvida");
                    jsonobjectProva.put("provaId", idProva);
                    jsonobjectProva.put("descricao", jsonobjectProvaAUX.get("descricao"));
                    jsonobjectProva.put("resolvida", resolvida);
                    jsonobjectProva.put("podeEditar", jsonobjectProvaAUX.get("podeEditar"));
                    
                    if(resolvida){
                        jsonobjectProva.put("notaFinal", jsonobjectProvaAUX.get("notaFinal"));
                    }
                    listProvas.add(jsonobjectProva);
                }
                
                request.setAttribute("usuario", usuario);
                request.setAttribute("disciplina", disciplina);
                request.setAttribute("listMaterias", materiasArray);
                request.setAttribute("listProvas", listProvas);
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/pages/disciplinaDetail.jsp");
                rd.forward(request, response);
            }
            
            if(function.equals("delete")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    disciplinaDAO.deleteDisciplina(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaCurso.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }

            if(function.equals("getDados")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    disciplina = disciplinaDAO.getById(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Gson gson = new Gson();
                String jsonDisciplina = gson.toJson(disciplina);
                
                out.println(jsonDisciplina);
                
            }
            
            if(function.equals("insert")){
                int id = Integer.parseInt((request.getParameter("id")));
                String nome = request.getParameter("nome");
                int cursoId = Integer.parseInt((request.getParameter("cursoId")));

                if(id > 0){
                    try {
                        disciplina = disciplinaDAO.getById(id);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                disciplina.setId(id);
                disciplina.setNome(nome);
                Curso curso = new Curso();
                CursoDAO cursoDAO = new CursoDAO();
                curso = cursoDAO.getById(cursoId);
                
                disciplina.setCurso(curso);
                
                if(disciplina.getId() != 0){
                    //update
                    try{
                        disciplinaDAO.update(disciplina);
                        out.println(true);
                    } catch (Exception ex){
                        out.println(false);
                    }
                }else{
                    //insert
                    try{
                        disciplinaDAO.insert(disciplina);
                        out.println(disciplina.getId());
                    } catch (Exception ex){
                        out.println(false);
                    }
                }

            }            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProcessaDisciplina.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProcessaDisciplina.class.getName()).log(Level.SEVERE, null, ex);
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
