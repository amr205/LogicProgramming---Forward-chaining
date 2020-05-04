/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototipo1;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author apaulina_mc
 */
public class Registro {
    int llave;
    boolean disponible;
    String Consecuente;
    ArrayList<String> Antecedentes;
    public Registro(){
        Antecedentes = new ArrayList<>();
    }
    
    @Override
    public String toString(){
        String s="";
        s+="LLAVE: "+llave+"\n";
        s+="CONSECUENTE: "+Consecuente+"\n";
        s+="ANTECEDENTES: "+Arrays.toString(Antecedentes.toArray())+"\n";
        return s;
    }
}
