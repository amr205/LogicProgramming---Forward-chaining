/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototipo1;

import java.util.ArrayList;

/**
 *
 * @author alejandro
 */
public class PredicadoV {
    String predicado;
    ArrayList<String> variables = new ArrayList<>();
    
    public PredicadoV(String ante){
        String[] split1=ante.split("\\(");
        predicado=split1[0];
        String[] split2=split1[1].split("\\)");
        String[] split3=split2[0].split(",");
        for (int i = 0; i < split3.length; i++) {
            variables.add(split3[i].replace(" ",""));
        }
        
    }
}
