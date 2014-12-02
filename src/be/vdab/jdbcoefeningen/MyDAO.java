/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.vdab.jdbcoefeningen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
/***
 *
 * @author Andy.Mertens
 */
public class MyDAO {

    private  String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
    private  String JDBC_MYSQL_CONNECTOR = "jdbc:mysql:";
    private  String DEFAULT_HOST = "localhost";
    private  String DEFAULT_PORT = "3306";
    
    private  Connection connDB=null;
    private  Statement myStatement = null;
    private  String urlDB="";
    
    public  Connection getConnectionToDB(String urlDB, String gebruiker, String paswoord){
        if(!(urlDB.contains("jdbc")))
            this.urlDB = this.JDBC_MYSQL_CONNECTOR + "//" + this.DEFAULT_HOST + ":" + this.DEFAULT_PORT + "/" + urlDB;
        else
            this.urlDB = urlDB;
        
        try{
            Class.forName(JDBC_DRIVER);
            getConnection(this.urlDB, gebruiker, paswoord);  
            return this.connDB;
            }
        catch(SQLException | ClassNotFoundException ex){
            System.err.println(ex.getMessage());
            return null;
        }
    }    
    
    public boolean closeConnectionToDB(){
        boolean isClosed=false;
        try{
            this.connDB.close();
            isClosed= true;
        }
        catch(SQLException ex){ System.err.println(ex.getMessage()); }
        finally{
            return isClosed;
        }
    }
                
    private void getConnection(String url, String user, String password)throws SQLException{
        System.out.println("Connecting to DB..." + url); 
        this.connDB = DriverManager.getConnection(url, user, password);  
        System.out.println("Connected to " + url);
    }   
    
    private boolean getStatement()throws SQLException{
        this.myStatement = this.connDB.createStatement();
        return !this.myStatement.isClosed();
    }
}