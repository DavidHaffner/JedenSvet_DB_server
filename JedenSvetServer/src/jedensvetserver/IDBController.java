/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jedensvetserver;

/**
 *
 * @author dhaffner
 */
public interface IDBController {
    
    public int doInsertToFilm(String jmenoFilmu, String rok, String reziser, String popis);
    public String doSelectFromFilm(String jmenoFilmu, String rok, String reziser, String popis);
    public int doUpdateToFilm(String idFilmu, String jmenoFilmu, String rok, String reziser, String popis);
    public String doSelectFromPristupy(String jmeno);
}
