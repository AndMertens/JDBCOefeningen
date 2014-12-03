/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.vdab.jdbcoefeningen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Andy.Mertens
 */
public class JDBCOefeningen {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String userBieren="bierAdmin";
        String paswoordBieren = "bieradmin";
        String dbBieren = "bieren";
        
        String userPlanten="plantAdmin";
        String paswoordPlanten = "plantadmin";
        String dbPlanten = "tuincentrum";
        String query;
        
        //CONNECTION
        MyDAO myDB = new MyDAO();
        Connection myConn = myDB.getConnectionToDB(dbPlanten, userPlanten, paswoordPlanten);
       
       
       //STATEMENT / QUERY
       ResultSet resRecords;
        try{
           query = "select indienst, voornaam, familienaam from werknemers where indienst >= {d '2003-03-01'}";
           PreparedStatement tmpStatement = myConn.prepareStatement(query);
           resRecords = tmpStatement.executeQuery();
                while(resRecords.next()){
                  System.out.print(resRecords.getDate("indienst"));
                  System.out.print(" ");
                  System.out.print(resRecords.getString("voornaam"));
                  System.out.print(" ");
                  System.out.println(resRecords.getString("familienaam"));
                }    
            }
       
        catch(SQLException ex){ex.getMessage();}
        finally{
            if(myConn!=null){
                try{
                   myConn.close();
               }
                catch(SQLException ex){System.err.println(ex.getMessage());}
            }
       } 
    }
       
    
}
