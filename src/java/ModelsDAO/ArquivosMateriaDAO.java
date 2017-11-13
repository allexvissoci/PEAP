/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelsDAO;

import Models.ArquivosMateria;
import Models.ConnectionFactory;
import Models.Materia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author allex
 */
public class ArquivosMateriaDAO {
    private Connection con = ConnectionFactory.get().getConnection();
    private String stmtSelectById = "select * From ArquivosMateria where id = ?;";
    private String stmtSelectByMateriaId = "select * From ArquivosMateria where materia = ?;";
    private String stmtInsert = "insert into ArquivosMateria (descricao, path, tipoArquivo, materia) values (?, ?, ?, ?);";
    private String stmtDelete = "delete from ArquivosMateria where id = ?;";

    public void deleteArquivo(int id) throws ClassNotFoundException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(stmtDelete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CursoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public List<ArquivosMateria> getArquivosByMateria(int materiaId) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectByMateriaId);
            stmt.setInt(1, materiaId);
            rs = stmt.executeQuery();
            List<ArquivosMateria> lstArquivos = new ArrayList();

            while (rs.next()) {
                ArquivosMateria arquivo = new ArquivosMateria();
                ArquivosMateriaDAO arquivoDAO = new ArquivosMateriaDAO();
                arquivo = arquivoDAO.getById(rs.getInt("id"));
                lstArquivos.add(arquivo);
            }
            return lstArquivos;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (Exception ex) {
                System.out.println("Erro ao fechar result set. Ex=" + ex.getMessage());
            };
            try {
                stmt.close();
            } catch (Exception ex) {
                System.out.println("Erro ao fechar stmt. Ex=" + ex.getMessage());
            };
            
        }
    }    
    
    public ArquivosMateria getById(int id) throws ClassNotFoundException {
        ArquivosMateria arquivo = new ArquivosMateria();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(stmtSelectById, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                arquivo.setId(rs.getInt("id"));
                arquivo.setDescricao(rs.getString("descricao"));
                arquivo.setPath(rs.getString("path"));
                arquivo.setTipoArquivo(rs.getString("tipoArquivo"));
                Materia materia = new Materia();
                MateriaDAO materiaDAO = new MateriaDAO();
                materia = materiaDAO.getById(rs.getInt("materia"));
                arquivo.setMateria(materia);
            }
            return arquivo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                stmt.close();
            } catch (Exception ex) {
                System.out.println("Erro ao fechar stmt. Ex=" + ex.getMessage());
            };
            
        }
    }
    
    public void insert(ArquivosMateria arquivo){
        PreparedStatement stmt = null;
        int idObjeto = 0;
        try {
            stmt = con.prepareStatement(stmtInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, arquivo.getDescricao());
            stmt.setString(2, arquivo.getPath());
            stmt.setString(3, arquivo.getTipoArquivo());
            stmt.setInt(4, arquivo.getMateria().getId());
            stmt.execute();
            //Seta o id 
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                idObjeto = rs.getInt(1);
            }

            arquivo.setId(idObjeto);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                stmt.close();
            } catch (Exception ex) {
                System.out.println("Erro ao fechar stmt. Ex=" + ex.getMessage());
            };
            
        }
    }       
}
