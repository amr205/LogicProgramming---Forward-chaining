/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototipo1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Itzel CG
 */
public class MotorDeInferencia { 
    ManejadorDeArchivo baseConocimiento= new ManejadorDeArchivo();
    
    public MotorDeInferencia(ManejadorDeArchivo man){
        baseConocimiento=man;
    }
    public ArrayList<String> encadenaAdelante(ArrayList<String> lista){
        ArrayList<String> baseHechos= lista;
        ArrayList<String> submetas = new ArrayList<>();
        ArrayList<Registro> reglas=baseConocimiento.leerTodos();
        int ultimaRegla=-1;
        boolean sigue=true;
        
        int [][] usadas= new int [reglas.size()][2];
        for (int i = 0; i < usadas.length; i++) {
            usadas [i][0]= reglas.get(i).llave;
            usadas [i][1]= 0;
           
        }
        
        while(sigue){
            System.out.println("------------------------------------------------------------------");
            ArrayList<Registro> conflicto= equiparacion(baseHechos,reglas, ultimaRegla);
            System.out.println("Conjunto conflicto: "+Arrays.toString(conflicto.toArray()));
            if(!conflicto.isEmpty()){
                Registro reglaAplicar = resolucion(conflicto,usadas);
                System.out.println("Regla a aplicar: "+reglaAplicar.toString());
                aplicarRegla(baseHechos, reglaAplicar, usadas, submetas, reglas);
                ultimaRegla = reglaAplicar.llave;
                
                //checar si ya se uso la regla 7 veces
                for (int i = 0; i < usadas.length; i++) {
                    if(usadas[i][0]==ultimaRegla&&usadas[i][1]>=7)
                        sigue=false;
                }
                
            }
            else
                sigue=false;
        }
        //System.out.println(Arrays.toString(baseHechos.toArray()));
        if(!baseHechos.isEmpty()){
            return submetas;
        }
        return null;
    }
    
    //obtener todas las reglas posibles a aplicar ( reglas que se podrian aplicar)
    public ArrayList<Registro> equiparacion(ArrayList<String> bh, ArrayList<Registro> reg, int ultimaU){
        ArrayList <String> bhp=new ArrayList <String>();
        for (int i = 0; i < bh.size(); i++) {
            PredicadoV temp=new PredicadoV(bh.get(i));
            bhp.add( temp.predicado);
        }
        ArrayList<ArrayList<String>> reglas = new ArrayList<>();
        for (int i = 0; i < reg.size(); i++) {
            ArrayList<String> tempo = new ArrayList<>();
            for (int j = 0; j < reg.get(i).Antecedentes.size(); j++) {
                PredicadoV p = new PredicadoV(reg.get(i).Antecedentes.get(j));
                tempo.add(p.predicado);    
            }
            reglas.add(tempo);
        }
        ArrayList<Registro> conf= new ArrayList<Registro>();
        for (int i = 0; i < reg.size(); i++) {
            if(bhp.containsAll(reglas.get(i))&&reg.get(i).llave!=ultimaU)
                conf.add(reg.get(i));
        }
    return conf;
    }
    
    private Registro resolucion(ArrayList<Registro> conflicto, int [][]usadas){
        int numVecesMenor=obtenerUsadas(conflicto.get(0).llave, usadas);
        int indice=0;
        
        for (int i = 0; i < conflicto.size(); i++) {
            int temp= obtenerUsadas(conflicto.get(i).llave, usadas);
            if (temp<numVecesMenor){
                indice=i;
                numVecesMenor=temp;
            }
                            
        }
        return conflicto.get(indice);
    }
    
    private boolean esSubmeta(String consecuente, ArrayList<Registro> reglas){
        boolean esSubmeta = true;
        PredicadoV cons = new PredicadoV(consecuente);
        
        for (int i = 0; i < reglas.size(); i++) {
            ArrayList<String> predicados = new ArrayList<>();
            for (int j = 0; j < reglas.get(i).Antecedentes.size(); j++) {
                PredicadoV temp = new PredicadoV(reglas.get(i).Antecedentes.get(j));
                predicados.add(temp.predicado);
            }

            if(predicados.contains(cons.predicado)){
                esSubmeta=false;
                break;
            }
        }
        return esSubmeta;
    }
    
    private int obtenerUsadas (int llave, int [][] usadas){
        for (int i = 0; i < usadas.length; i++) {
            if (usadas[i][0]==llave)
                return usadas[i][1];
            
        } 
        return 0;
    }
    
    private void aplicarRegla(ArrayList<String> bh, Registro reg, int [][] usadas, ArrayList<String> submetas, ArrayList<Registro> reglas){
        HashMap<String, ArrayList<String>> variablesFiltradas = new HashMap<String, ArrayList<String>>();
        System.out.println("Aplicando regla");
        for (int i = 0; i < reg.Antecedentes.size(); i++) {
            System.out.println("antecedente a analizar: "+reg.Antecedentes.get(i));
            //Paso 1: Obtener variables actuales 
            PredicadoV ante= new PredicadoV(reg.Antecedentes.get(i));
            ArrayList <String> varAnt=ante.variables;
            ArrayList<ArrayList<String>> bhc = new ArrayList<>();
            
            
            for (int j = 0; j < bh.size(); j++) {
                PredicadoV temp =new PredicadoV(bh.get(j));
                if (temp.predicado.equals(ante.predicado)) {
                    bhc.add(temp.variables);
                }
            }
            
            
            System.out.println("Base de hechos cucha: ");
            for (int j = 0; j < bhc.size(); j++) {
                System.out.println(Arrays.toString(bhc.get(j).toArray()));
            }
            
            
            
            HashMap<String, ArrayList<String>> hechosActuales = new HashMap<String, ArrayList<String>>();
            for (int j = 0; j < varAnt.size(); j++) {
                ArrayList<String> sustitucion = new ArrayList<String>();
                for (int k = 0; k < bhc.size(); k++) {
                    sustitucion.add(bhc.get(k).get(j));
                }
                hechosActuales.put(varAnt.get(j), sustitucion);
            
            }
            System.out.println("Hechos actuales del antecedente: "+hechosActuales.toString());
            //paso 2 filtrado
            // Print keys and values
            for (String key : hechosActuales.keySet()) {
              //System.out.println("key: " + key + " value: " + hechosActuales.get(key));
              if(variablesFiltradas.containsKey(key)){
                  ArrayList<String> varAfil = hechosActuales.get(key);
                  for (int x = 0; x < varAfil.size(); x++) {
                      String var = varAfil.get(x);
                      if(!variablesFiltradas.get(key).contains(var)){
                          for (String key2 : hechosActuales.keySet()) {
                              //System.out.println("key: " + key2 + " value: " + hechosActuales.get(key2));
                              ArrayList<String> nueVars = hechosActuales.get(key2);
                              nueVars.remove(x);
                              hechosActuales.put(key2, nueVars);
                          }
                      }
                  }
                  
              }
            }
            variablesFiltradas=hechosActuales;
        }
        //PASO 3 DEVOLVER HECHOS GENERADOS
        Set<String> keys = variablesFiltradas.keySet();
        if(!keys.isEmpty()){
            int numHG = variablesFiltradas.get(keys.toArray()[0]).size();
            PredicadoV cons = new PredicadoV(reg.Consecuente);
            for (int o = 0; o < numHG; o++) {
                String hechoNew = cons.predicado+"(";
                for (int l = 0; l < cons.variables.size(); l++) {
                    String realVar = variablesFiltradas.get(cons.variables.get(l)).get(o);
                    if(l==0){
                        hechoNew+=realVar;
                    }else{
                        hechoNew+=","+realVar;
                    }
                }
                hechoNew+=")";
                if(!bh.contains(hechoNew)){
                    bh.add(hechoNew);
                    if(esSubmeta(hechoNew,reglas)){
                        submetas.add(hechoNew);
                    }
                }
            }
        }
        //PASO 4 SUMAR A USADAS
        for (int p = 0; p < usadas.length; p++) {
            if(usadas[p][0]==reg.llave)
                usadas[p][1]=usadas[p][1]+1;
        }
    }
    
    
}
