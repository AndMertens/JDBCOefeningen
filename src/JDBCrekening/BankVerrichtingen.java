/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBCrekening;

import java.util.Scanner;
import java.util.InputMismatchException;  
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;

/**
 *
 * @author Andy.Mertens
 */
public class BankVerrichtingen {
    
    private final static RekeningManager rekeningen=new RekeningManager();   
    private enum Menu {
        STOPPEN(0), NIEUWE_REKENING(1), CONSULTEER_REKENING(2), OVERSCHRIJVEN(3),ERROR(99);
        
        private final int Value;
        private Menu(int value){ Value= value;}
        
        // Mapping Menu to Menu ID
        private static final Map<Integer, Menu> _map = new HashMap<Integer, Menu>();
        static
        {
            for (Menu menu : Menu.values())
            _map.put(menu.Value, menu);
        }
 
        public static Menu getKeuze(int value)
        {
            if (value>3 || value<0)
                return _map.get(99);
            else
                return _map.get(value);
        }
        
    }
    
    public static void main(String [] args){
        
        
        boolean flagContinue=true;
        Scanner sc = new Scanner(System.in);
        while(flagContinue){
        
            toonMenu();
                        
            try{
                Menu keuze = Menu.getKeuze(sc.nextInt());
                switch(keuze){
                    case NIEUWE_REKENING:
                        maakNieuweRekening(sc);
                        break;
                    case CONSULTEER_REKENING:
                        consulteerRekening(sc);
                        flagContinue = false;
                        break;
                    case OVERSCHRIJVEN:
                        doeOverschrijving(sc);
                        flagContinue = false;
                        break;
                    case ERROR:
                        System.out.println("Ongeldige keuze. Voer andere keuze in");
                        break;
                    case STOPPEN:
                        flagContinue = false;
                        break;
                        
                }
            }
            
            catch(InputMismatchException ex){
                System.out.println("Ongeldige input. Programma stopgezet");
                flagContinue = false;
            }
            catch(Exception ex){
                System.err.println(ex.getMessage());
                flagContinue = false;
            }
        }
    }
    
   private static void toonMenu(){
       
        System.out.println("Maak keuze");
        System.out.println("----------");
        System.out.println("0. Stoppen");
        System.out.println("1.  Nieuwe Rekening");
        System.out.println("2.  ConsulteerRekening");
        System.out.println("3.  SchrijfBedragOver");
        System.out.println("____________________________");
    }
   
    private static void consulteerRekening(Scanner sc){
        System.out.println("Geef het te consulteren rekeningnummer (12 cijfers):");
        String rekeningNr = sc.next();
        try{
        rekeningen.consulteerRekening(Long.parseLong(rekeningNr));
        }
        catch(RekeningException ex){System.err.println(ex.getMessage());}
    }
   
    private static void maakNieuweRekening(Scanner sc){
        System.out.println("Geef het rekeningnummer (12 cijfers):");
        String rekeningNr = sc.next();
        try{
        rekeningen.maakEenRekeningAan(Long.parseLong(rekeningNr));
        }
        catch(RekeningException ex){ex.getMessage();}
    }
       
    private static void doeOverschrijving(Scanner sc){
        System.out.println("Geef het rekeningnummer waar bedrag moet afgehaald worden (12 cijfers):");
        String vanRekeningNr = sc.next();
        System.out.println("Geef het rekeningnummer waarop bedrag moet overgeschreven worden (12 cijfers) :");
        String naarRekeningNr = sc.next();
        System.out.println("Geef het bedrag dat moet worden overgeschreven :");
        BigDecimal bedrag = new BigDecimal(sc.next());
        
        try{
            rekeningen.schrijfBedragOver(Long.parseLong(vanRekeningNr), Long.parseLong(naarRekeningNr), bedrag);
        }
        catch(RekeningException ex){ex.getMessage();}
    }
}
