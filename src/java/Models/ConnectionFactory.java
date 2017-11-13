/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author allex
 */
public class ConnectionFactory {
    private static ConnectionFactory self = new ConnectionFactory();
    private Connection conn;
    
    public ConnectionFactory() {
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
            conn = DriverManager.getConnection("jdbc:mysql://localhost/smartquestion", "mysqldb", "mysenha");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static ConnectionFactory get(){
        return self;
    }
    
    public Connection getConnection(){
        return conn;
    }        
}
