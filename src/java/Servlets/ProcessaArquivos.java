/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Models.ArquivosMateria;
import Models.Materia;
import ModelsDAO.ArquivosMateriaDAO;
import ModelsDAO.MateriaDAO;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author allex
 */
@WebServlet(name = "ProcessaArquivos", urlPatterns = {"/ProcessaArquivos"})
@MultipartConfig
public class ProcessaArquivos extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String SAVE_DIR = "uploadFiles";

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileNotFoundException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            String function = request.getParameter("function");
            
            ArquivosMateria arquivo = new ArquivosMateria();
            ArquivosMateriaDAO arquivoDAO = new ArquivosMateriaDAO();
            
            
            if(function.equals("uploadArquivo")){
                int materiaId = Integer.parseInt((request.getParameter("materiaId")));
                int disciplinaId = Integer.parseInt((request.getParameter("disciplinaId")));
                
                Materia materia = new Materia();
                MateriaDAO materiaDAO = new MateriaDAO();
                materia = materiaDAO.getById(materiaId);
                

                InputStream inputStream;
                FileOutputStream fileOutputStream;
                for (Part part : request.getParts()) {
                    inputStream = request.getPart(part.getName()).getInputStream();
                    int i = inputStream.available();
                    byte[] b = new byte[i];
                    inputStream.read(b);
                    String fileName = "";

                    for (String temp : part.getHeader("content-disposition").split(";")) {
                        if (temp.trim().startsWith("filename")) {
                            fileName = temp.substring(temp.indexOf('=') + 1).trim().replace("\"", "");
                        }
                    }
                    
                    String appPath = request.getServletContext().getRealPath("").replace("\\build\\", "\\");
                    String uploadDir = appPath + File.separator + SAVE_DIR;
                    
                    arquivo.setDescricao(fileName);
                    arquivo.setPath(uploadDir);
                    arquivo.setTipoArquivo(fileName.substring(fileName.lastIndexOf(".") + 1));
                    arquivo.setMateria(materia);
                    arquivoDAO.insert(arquivo);
                    
                    File pasta = new File(uploadDir);
                    if(!pasta.exists()){
                        pasta.mkdir();
                    }
                    
                    fileOutputStream = new FileOutputStream(uploadDir + "/" + arquivo.getId()+"_"+fileName);
                    fileOutputStream.write(b);
                    inputStream.close();
                    fileOutputStream.close();

                }
                response.sendRedirect(request.getContextPath()+"/ProcessaDisciplina?function=disciplinaDetail&id="+disciplinaId);

            }
            
            if(function.equals("delete")){
                int id = Integer.parseInt((request.getParameter("id")));
                try {
                    arquivoDAO.deleteArquivo(id);
                    
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProcessaCurso.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProcessaArquivos.class.getName()).log(Level.SEVERE, null, ex);
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
