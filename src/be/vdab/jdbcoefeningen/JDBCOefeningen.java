/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.vdab.jdbcoefeningen;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Andy.Mertens
 */
public class JDBCOefeningen {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String user="plantAdmin";
        String paswoord = "plantadmin";
        String db = "tuincentrum";
        
       MyDAO myDB = new MyDAO();
       Connection myConn = myDB.getConnectionToDB(db, user, paswoord);
       try{
            Statement myStatement = myConn.createStatement();
            ResultSet resRecords = myStatement.executeQuery(db);
            Array namen = resRecords.getArray("naam");
            for()
                System.out.println(resRecords.)
       }
       catch(SQLException ex){System.err.println(ex.getMessage());}
        
        
    }
}
