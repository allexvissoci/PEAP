/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Models.Curso;
import Models.TipoUsuario;
import Models.Usuario;
import ModelsDAO.CursoDAO;
import ModelsDAO.TipoUsuarioDAO;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author allex
 */
@WebServlet(name = "ProcessaUsuario", urlPatterns = {"/ProcessaUsuario"})
public class ProcessaUsuario extends HttpServlet {

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
            
            Usuario usuario = new Usuario();
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            
            if(function == null){

                List<Usuario> listaUsuario = new ArrayList();
                try {
                    listaUsuario = usuarioDAO.getLista();

                    request.setAttribute("listUsuario", listaUsuario);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/pages/usuario.jsp");
                    rd.forward(request, response);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            

            
            if(function.equals("toSelect")){
                List<Usuario> listaUsuarios = usuarioDAO.getListaToSelect();
                JSONArray jsonarray = new JSONArray();
                for(Usuario usu : listaUsuarios){
                    JSONObject object = new JSONObject();
                    object.put("id", usu.getId());
                    object.put("nome", usu.getNome());
                    jsonarray.add(object);
                }
                out.println(jsonarray);
            }
            
            if(function.equals("getTipoUsuario")){
                TipoUsuarioDAO tipoUsuarioDAO = new TipoUsuarioDAO();
                List<TipoUsuario> lstTipoUsuario = new ArrayList();
                try {
                    lstTipoUsuario = tipoUsuarioDAO.getListaTipoUsuario();
                    
                    Gson gson = new Gson();
                    String jsonTipoUsuario = gson.toJson(lstTipoUsuario);
                    
                    out.println(jsonTipoUsuario);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            }
            
            if(function.equals("resetPassword")){
                int id = Integer.parseInt((request.getParameter("id")));
                String senha = request.getParameter("senha");
                try {
                    usuario = usuarioDAO.getById(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                usuario.setSenha(senha);
                
                try{
                    usuarioDAO.resetPassword(usuario);
                    out.println(true);
                } catch (Exception ex){
                    out.println(false);
                }
                
                String jsonUsuario;
                if(usuario.getId() > 0){
                    Gson gson = new Gson();
                    jsonUsuario = gson.toJson(usuario);
                }else{
                    jsonUsuario = "";
                }
                
                out.println(jsonUsuario);
                
            }
            
            if(function.equals("getDados")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    usuario = usuarioDAO.getById(id);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Gson gson = new Gson();
                String jsonUsuario = gson.toJson(usuario);
                
                out.println(jsonUsuario);
                
            }
            
            if(function.equals("insert")){
                int id = Integer.parseInt((request.getParameter("id")));
                String nome = request.getParameter("nome");
                String senha = request.getParameter("senha");
                int tipoUsuarioid = Integer.parseInt((request.getParameter("tipo_usuario")));
                TipoUsuario tipoUsuario = new TipoUsuario();
                TipoUsuarioDAO tipoUsuarioDAO = new TipoUsuarioDAO();
                try {
                    tipoUsuario = tipoUsuarioDAO.getById(tipoUsuarioid);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                Boolean status = Boolean.parseBoolean(request.getParameter("status"));

                if(id > 0){
                    try {
                        usuario = usuarioDAO.getById(id);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                usuario.setId(id);
                usuario.setNome(nome);
                usuario.setStatus(status);
                usuario.setSenha(senha);
                usuario.setTipoUsuario(tipoUsuario);


                if(usuario.getId() != 0){
                    //update
                    try{
                        usuarioDAO.update(usuario);
                        out.println(true);
                    } catch (Exception ex){
                        out.println(false);
                    }
                }else{
                    //insert
                    try{
                        usuarioDAO.insert(usuario);
                        out.println(usuario.getId());
                    } catch (Exception ex){
                        out.println(false);
                    }
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProcessaUsuario.class.getName()).log(Level.SEVERE, null, ex);
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
