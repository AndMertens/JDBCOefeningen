/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBCrekening;
    
import java.math.BigDecimal;

/**
 *
 * @author Andy.Mertens
 */
public class Rekening {
    
    
    private long rekeningNr;
    private BigDecimal saldo = new BigDecimal(0);
    
    public long getRekeningNr() { return rekeningNr; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo){ this.saldo = saldo;}
        
    public Rekening(long rekeningnr)throws RekeningException{
        this.rekeningNr = rekeningnr;
    }
    
    public Rekening(long rekeningnr, BigDecimal saldo){
           this.rekeningNr = rekeningnr;
           this.saldo = saldo;
    }
    
    @Override public String  toString(){
        StringBuilder strRekening = new StringBuilder("");
        strRekening.append("Rekening : ");
        strRekening.append(this.getRekeningNr());
        strRekening.append("\t Saldo : ");
        strRekening.append(this.getSaldo().toString());
        strRekening.append(" Euro");
        
        return strRekening.toString();
    }
    
    @Override public boolean equals(Object o){
        if(!(o instanceof Rekening)) return false;
        else{
            Rekening ander = (Rekening)o;
            return this.rekeningNr == ander.getRekeningNr();
        }
    }
    
    @Override public int hashCode(){
        return this.toString().hashCode();
    }

    
}