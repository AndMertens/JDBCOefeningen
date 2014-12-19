/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.vdab.jdbcoefeningen;

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
        
        String userBieren="bierAdmin";
        String paswoordBieren = "bieradmin";
        String dbBieren = "bieren";
        
        String userPlanten="plantAdmin";
        String paswoordPlanten = "plantadmin";
        String dbPlanten = "tuincentrum";
        String UpdateSQL1 = "update bieren set brouwerid = 2 where (brouwerid = 1 and alcohol >= 8.50)";
        String UpdateSQL2 = "update bieren set brouwerid = 3 where (brouwerid = 1 and alcohol < 8.50)";
        String UpdateSQL3  ="delete from brouwers where id=1";
        
        
        //CONNECTION
        MyDAO myDB = new MyDAO();
        Connection myConn = myDB.getConnectionToDB(dbBieren, userBieren, paswoordBieren);
              
       //STATEMENT / QUERY
                              
            ResultSet resRecords;
            try{
                myConn.setAutoCommit(false);
                Statement tmpStatement = myConn.createStatement();
                tmpStatement.addBatch(UpdateSQL1);
                tmpStatement.addBatch(UpdateSQL2);
                //tmpStatement.addBatch(UpdateSQL3);
                tmpStatement.executeBatch();
                myConn.commit();
                 
            }
       
        catch(SQLException ex){ex.printStackTrace();}
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
