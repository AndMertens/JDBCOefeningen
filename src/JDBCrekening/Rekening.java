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

/**
 *
 * @author Andy.Mertens
 */
public class Rekening {
    
    private final int CONTROLE_GETAL_REF = 97;
    private long RekeningNr;
    private BigDecimal saldo = new BigDecimal(0);
    
    private Connection conn;
    private PreparedStatement myPreparedStatement;
     
    private final String QUERY_ZOEK_REKENING="select RekeningNr from rekeningen where RekeningNr=?";
    private final String QUERY_MAAK_REKENING="insert into rekeningen(RekeningNr,Saldo) values(?,?)";
    //private final String QUERY_VIND_SALDO = "select Saldo from rekeningen where RekeningNr=?";
    private final String QUERY_SET_SALDO = "update rekeningen(Saldo) values(?) where RekeningNr =?";
    
    private final String USER_REKENING="adminRekeningen";
    private final String PASWOORD_REKENING="adminrekeningen";
    private final String DATABASE_REKENING="rekeningen";
    private final String KOLOM_REKENING_NUMMER="RekeningNr";
    private final String KOLOM_REKENING_SALDO="RekeningNr";
        
    public Rekening(long rekeningnr, BigDecimal saldo, boolean flagIsNieuw){
        
        try{
            valideerRekening(rekeningnr);
            if(flagIsNieuw) maakEenRekeningAan(rekeningnr, saldo);
            haalGegevensOp(rekeningnr);
        }
        catch(RekeningException | SQLException ex){System.err.println(ex.getMessage());}
    }
    
    public Rekening(long rekeningnr, boolean flagIsNieuw){
        
       try{
            valideerRekening(rekeningnr);
            if(flagIsNieuw) maakEenRekeningAan(rekeningnr, this.saldo);
            haalGegevensOp(rekeningnr);
        }
        catch(RekeningException | SQLException ex){System.err.println(ex.getMessage());}
    }
    
    public void setSaldo(BigDecimal saldo){
        try{
           pasSaldoAan(saldo);     
        }
        catch(SQLException ex){System.err.println(ex.getMessage());}
        finally{
            try{
                
            }
            catch(Exception ex){System.out.println(ex.getMessage());}
        }
  }
      
    public String consulteerRekening(){
        return this.toString();
    }
    
    private void valideerRekening(long rekeningnr)throws RekeningException{ 
        
        String rekening = String.valueOf(rekeningnr);
        long GetalRekening = Long.parseLong(rekening.substring(0, 11));
        long controlegetal = rekeningnr/100;
                
        if(!(rekening.length()==12))
            throw new RekeningException("Ongeldig. Een rekening Nummer moet bestaan uit 12 getallen");
        
        if( ((int)(GetalRekening / CONTROLE_GETAL_REF)) == (int)controlegetal)
            this.RekeningNr = rekeningnr;
        else
            throw new RekeningException("Ongeldig rekeningnummer");
    }

    private void maakEenRekeningAan(long rekeningnr, BigDecimal saldo){
       
        ResultSet resultset;
        
        try{
            conn = DriverManager.getConnection(USER_REKENING, PASWOORD_REKENING, DATABASE_REKENING);
            conn.setAutoCommit(false);
            myPreparedStatement = conn.prepareStatement(QUERY_MAAK_REKENING,PreparedStatement.RETURN_GENERATED_KEYS);
            myPreparedStatement.setLong(1, rekeningnr);
            myPreparedStatement.setBigDecimal(2, saldo);
            resultset = myPreparedStatement.getGeneratedKeys();
            if(resultset.first()){
                conn.commit();
                System.out.println(resultset.getLong(1));
            }
        }
        catch(SQLException ex){System.out.println(ex.getMessage());} 
        finally{
            try{ if(!conn.isClosed()) conn.close();}
            catch(SQLException ex){System.out.println(ex.getMessage());}
        }
    }
        
    private void haalGegevensOp(long rekeningnr)throws SQLException{
              
        conn = DriverManager.getConnection(USER_REKENING, PASWOORD_REKENING, DATABASE_REKENING);
        myPreparedStatement = conn.prepareStatement(QUERY_ZOEK_REKENING);
        myPreparedStatement.setLong(1, rekeningnr);
       
        ResultSet resultset =  myPreparedStatement.executeQuery();  
        if(resultset.first()){
            resultset.next();
            this.RekeningNr = resultset.getLong(KOLOM_REKENING_NUMMER);
            this.saldo = resultset.getBigDecimal(KOLOM_REKENING_SALDO);
        }
    }
     
    private void pasSaldoAan(BigDecimal saldo)throws SQLException{
        conn = DriverManager.getConnection(USER_REKENING, PASWOORD_REKENING, DATABASE_REKENING);
        conn.setAutoCommit(false);
        myPreparedStatement = conn.prepareStatement(QUERY_SET_SALDO);
        myPreparedStatement.setBigDecimal(1, saldo);
        myPreparedStatement.setLong(2, this.RekeningNr);
        int resultset =  myPreparedStatement.executeUpdate();  
        if (resultset==1){
            conn.commit();
            this.saldo = saldo;
        }
    }
    
    @Override public String  toString(){
        StringBuilder strRekening = new StringBuilder("");
        strRekening.append("Rekening : ");
        strRekening.append(this.RekeningNr);
        strRekening.append("\t Saldo : ");
        strRekening.append(this.saldo.toString());
        strRekening.append(" Euro");
        
        return strRekening.toString();
    }
     
}
