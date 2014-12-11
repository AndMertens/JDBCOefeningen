/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBCrekening;

/**
 *
 * @author Andy.mertens
 */
public interface Valideer {
    
    public boolean valideerRekening(long rekeningnr)throws RekeningException ;
}
