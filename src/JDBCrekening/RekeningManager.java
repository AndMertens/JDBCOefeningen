/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBCrekening;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

/**
 *
 * @author Andy.mertens
 */
public class RekeningManager implements Valideer {
    
    private final int CONTROLE_GETAL_REF = 97;
    
    private Hashtable<Long,Rekening> Rekeningen = new Hashtable<Long,Rekening>();
    
    private Connection conn;
    private PreparedStatement myUpdateStatement;
    private Statement myQueryStatement;
     
    private final String QUERY_ZOEK_REKENINGEN="select RekeningNr,Saldo from rekeningen";
    private final String QUERY_MAAK_REKENING="insert into rekeningen(RekeningNr,Saldo) values(?,0)";
    private final String QUERY_SET_SALDO = "update rekeningen set Saldo =? where RekeningNr =?";
    
    private final String USER_REKENING="adminRekeningen";
    private final String PASWOORD_REKENING="adminrekeningen";
    private final String DATABASE_REKENING="jdbc:mysql://localhost:3306/rekeningen";
    private final String KOLOM_REKENING_NUMMER="RekeningNr";
    private final String KOLOM_REKENING_SALDO="Saldo";
    
    
  
    public RekeningManager(){
        
        try{
            //Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DATABASE_REKENING, USER_REKENING, PASWOORD_REKENING);
            
            myQueryStatement = conn.createStatement();
            ResultSet resultset = myQueryStatement.executeQuery(QUERY_ZOEK_REKENINGEN);
            while(resultset.next()){
                   System.err.print(String.valueOf(resultset.getLong("RekeningNr")));
                   System.err.print("-");
                   System.err.println(String.valueOf(resultset.getBigDecimal("Saldo")));
                   Rekeningen.put(resultset.getLong("RekeningNr"),new Rekening(resultset.getLong("RekeningNr"),resultset.getBigDecimal("Saldo")));
            }
        }
        catch(SQLException ex){System.err.println(ex.getMessage());}
        finally{
            try{ if (!conn.isClosed()) conn.close();}
            catch(Exception ex){ System.err.println(ex.getMessage());}
        }
    }
    
    public String consulteerRekening(long rekeningnr){
        
        String strConsult="";
        
        try{
            valideerRekening(rekeningnr);
        
            Rekening tmpRekening =getRekening(rekeningnr);
            if(tmpRekening== null)
                strConsult = "Rekening (" + String.valueOf(rekeningnr) + ") bestaat niet";
            else
                strConsult = tmpRekening.toString();
        }
        catch(RekeningException ex){System.err.println(ex.getMessage());}
        
        finally{
            return strConsult;
        }
    }

    
    
    public void maakEenRekeningAan(long rekeningnr){
       
        Rekening tmpRekening;
        ResultSet resultset;
        
        
        try{
                valideerRekening(rekeningnr);
                if (!Rekeningen.containsKey(rekeningnr))
                {
                    tmpRekening = new Rekening(rekeningnr);
                
                    try{
                        conn = DriverManager.getConnection(DATABASE_REKENING, USER_REKENING, PASWOORD_REKENING);
                        conn.setAutoCommit(false);
                        myUpdateStatement = conn.prepareStatement(QUERY_MAAK_REKENING,PreparedStatement.RETURN_GENERATED_KEYS);
                        myUpdateStatement.setLong(1, rekeningnr);
                        myUpdateStatement.executeUpdate();
                        conn.commit();
                        Rekeningen.put(rekeningnr, tmpRekening);
                        System.out.println("Nieuwe rekening aangemaakt : "+ tmpRekening.toString());
                    }
                    
                    catch(SQLException ex){System.err.println(ex.getMessage());} 
                    finally{
                        try{ if(!conn.isClosed()) conn.close();}
                        catch(Exception ex){System.err.println(ex.getMessage());}
                    }
                }//end try SQL
            //}//end if valideerrekening
        }//end try valideer
        catch(RekeningException ex){System.out.println(ex.getMessage());} 
        finally{
            try{ if(!conn.isClosed()) conn.close();}
            catch(Exception ex){System.out.println(ex.getMessage());}
        }
    }
     
    public void schrijfBedragOver(long vanRekeningNr, long naarRekeningNr, BigDecimal bedrag) throws RekeningException {
        
        Rekening vanRekening;
        Rekening naarRekening;
        BigDecimal nieuwSaldo;
        
        if(bedrag.doubleValue()<0)
            throw new RekeningException("Bedrag moet groter dan nul zijn");
        
        try{        
            valideerRekening(vanRekeningNr);
            valideerRekening(naarRekeningNr);
            
            //rekeningen moeten alletwee beschikbaar zijn
            if(!Rekeningen.containsKey(vanRekeningNr))
                throw new RekeningException("Rekening " + String.valueOf(vanRekeningNr) + " bestaat niet");
        
            if(!Rekeningen.containsKey(naarRekeningNr))
                throw new RekeningException("Rekening " + String.valueOf(naarRekeningNr)+ "bestaat niet");
            {
                           
                try{
                    conn = DriverManager.getConnection(DATABASE_REKENING, USER_REKENING, PASWOORD_REKENING);
                    conn.setAutoCommit(false);
                    myUpdateStatement = conn.prepareStatement(QUERY_SET_SALDO);

                    //batch statements voor vanRekening
                    myUpdateStatement.setBigDecimal(1, Rekeningen.get(vanRekeningNr).getSaldo().subtract(bedrag));
                    myUpdateStatement.setLong(2, vanRekeningNr);
                    myUpdateStatement.addBatch();

                    //batch statements voor naarRekening
                   myUpdateStatement.setBigDecimal(1, Rekeningen.get(vanRekeningNr).getSaldo().add(bedrag));
                    myUpdateStatement.setLong(2, naarRekeningNr);
                    myUpdateStatement.addBatch();

                    //check all db statements are ok
                    int resultset[] =  myUpdateStatement.executeBatch();  
                    if (resultset.length==2){
                        conn.commit();
                        Rekeningen.get(vanRekeningNr).setSaldo(Rekeningen.get(vanRekeningNr).getSaldo().subtract(bedrag));
                        Rekeningen.get(naarRekeningNr).setSaldo(Rekeningen.get(naarRekeningNr).getSaldo().add(bedrag));
                        System.out.println(Rekeningen.get(vanRekeningNr).toString());
                         System.out.println(Rekeningen.get(naarRekeningNr).toString());
                    }
                }           
                catch(SQLException ex){System.err.println(ex.getMessage());}
                finally{
                    try{
                        if(!conn.isClosed()) conn.close();
                    }
                    catch(Exception ex){System.err.println(ex.getMessage());}
                }
            }
        }
        catch(RekeningException ex){System.err.println(ex.getMessage());}
       
    }
    
   @Override public boolean valideerRekening(long rekeningnr)throws RekeningException{ 
        boolean isValid = false;
        
        String rekening = String.valueOf(rekeningnr);
        if(!(rekening.length()==12))
           throw new RekeningException("Rekeningnummer moet bestaan uit 12 cijfers");
               
        long GetalRekening = Long.parseLong(rekening.substring(0, 10));
        int controlegetal = Integer.parseInt(rekening.substring(10,rekening.length()));
        
        if( !(((int)(GetalRekening % (CONTROLE_GETAL_REF)) % (int)controlegetal) == 0))
           throw new RekeningException("Ongeldig Rekeningnummer. Controlegetal klopt niet");
        
        return isValid;
        
    }   
   
      private Rekening getRekening(long rekeningnr){
       
          System.err.println("in functie get rekening");
          
       Rekening tmpRekening = null;
          
       if(!Rekeningen.isEmpty()){
           if(Rekeningen.containsKey(rekeningnr))
               tmpRekening = Rekeningen.get(rekeningnr);
       }
       return tmpRekening;
    }
}
