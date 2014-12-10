/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBCrekening;

import java.util.Scanner;

/**
 *
 * @author Andy.Mertens
 */
public class BankVerrichtingen {
    
    public enum Menu {
    NIEUWE_REKENING, SALDO, OVERSCHRIJVEN
}
    
    public static void main(String [] args){
    
        
    
    }
    
    private void consulteerRekening(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Geef het te consulteren rekeningnummer (12 cijfers):");
        String rekeningNr = sc.next();
        Rekening mijnRekening = new Rekening(Long.parseLong(rekeningNr),false);
        System.out.println(mijnRekening.consulteerRekening());
        
    }
    
    private void maakNieuweRekening(){
        
    }
    
    private void schrijfBedragOver(){
        
    }
}
