/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototipo1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author apaulina_mc
 */
public class ManejadorDeArchivo {
    RandomAccessFile indice;
    RandomAccessFile maestro;
    ArbolBinario arbol_indice;
    public void insertar(Registro registro){
        int n =1;
        try{
            File archivo = new File("indice.dat");
            if(archivo.exists()){
                n=(int)((archivo.length()))/9+1;
            }
            indice.seek(indice.length());
            maestro.seek(maestro.length());
            indice.writeInt(n);
            indice.writeInt(n);
            indice.writeBoolean(true);
            registro.llave=n;
            maestro.writeInt(n);
            maestro.writeChars(formatearString(registro.Consecuente));
            for (int i = 0; i < 20; i++) {
                if(i<registro.Antecedentes.size()){
                    maestro.writeChars(formatearString(registro.Antecedentes.get(i)));
                }
                else{
                    maestro.writeChars(formatearString(""));
                }
            }
            maestro.writeBoolean(true);
            arbol_indice.raiz=null;
            //System.out.println("Se añadio un nuevo registro con llave: "+n);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public String formatearString(String stri){
        return String.format("%1$50s",stri);
    }
    
    public void cerrar(){
        try {
            indice.close();
            maestro.close();
        }catch(Exception e){ }
                
    }
    
    public void eliminar(int llave){
        llenarArbol();
        //debemos de buscar la direccion del registro a eliminarx
        ArbolBinario.Nodo nodo = arbol_indice.buscar(arbol_indice.raiz, llave);
        if(nodo==null){
            JOptionPane.showMessageDialog(null,"No existe el registro con la llave ingresada");
            //System.out.println("No existe el registro con la llave ingresada");
            return;
        }
        Registro r = consultar(llave);
        //System.out.println("Nodo "+nodo.direccion);
        try{
            indice.seek((nodo.direccion-1)*9);
            indice.writeInt(nodo.llave); /*nodo.Nodo.this.llave*/
            indice.writeInt(nodo.direccion);
            indice.writeBoolean(false);
            maestro.seek((nodo.direccion-1)*2105+2104);
            maestro.writeBoolean(false);
            arbol_indice.raiz=null;
            //System.out.println("Se eliminó el registro con llave: "+llave);
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
       
    }
    public void actualizar(int llave, Registro nuevo){
         llenarArbol();
        //debemos de buscar la direccion del registro a eliminarx
        ArbolBinario.Nodo nodo = arbol_indice.buscar(arbol_indice.raiz, llave);
        if(nodo==null){
            //System.out.println("No existe el registro con la llave ingresada");
            return;
        }
        //System.out.println("Nodo "+nodo.direccion);
        try{
            indice.seek((nodo.direccion-1)*9);
            indice.writeInt(nodo.llave); /*Nodo.this.llave???*/
            indice.writeInt(nodo.direccion);
            indice.writeBoolean(true);
            maestro.seek((nodo.direccion-1)*2105+4);
            maestro.writeChars(formatearString(nuevo.Consecuente));
            for (int i = 0; i < 20; i++) {
                if(i<nuevo.Antecedentes.size()){
                    maestro.writeChars(formatearString(nuevo.Antecedentes.get(i)));
                }
                else{
                    maestro.writeChars(formatearString(""));
                }
            }
            //System.out.println("Se actualizó el registro con llave: "+llave);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private String leerCadena() throws IOException{
        String s ="";
        for (int i = 0; i < 50; i++) {
            s += maestro.readChar();
        }
        return s;
    }
    public Registro consultar(int llave){
        llenarArbol();
        //buscar la dirección del registro que queremos 
        ArbolBinario.Nodo nodo = arbol_indice.buscar(arbol_indice.raiz, llave);
        if(nodo==null){
            //System.out.println("No existe el registro con la llave ingresada");
            return null;
        }
        //System.out.println("Nodo "+nodo.direccion);
        //leer el registro de el archivo maestro en esta direccion
        Registro temp = new Registro();
        try {
            maestro.seek((nodo.direccion-1)*2105);
            temp.llave= maestro.readInt();
            temp.Consecuente = leerCadena().replaceAll(" ", "");
            for (int i = 0; i < 20; i++) {
                String cadena = leerCadena();
                String cade = cadena.replaceAll(" ", "");
                if(!cade.isEmpty()){ //Solo muestra los que no estan vacios 
                    temp.Antecedentes.add(cade);
                }
            }
            temp.disponible = maestro.readBoolean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
        //como parametro se debe tomar la llave de consultar
        
    }
    private void llenarArbol(){
        
        if(arbol_indice.raiz==null){
            //leer cada registro del archivo indice y meterlo al arbol
            try {
                File archivo = new File("indice.dat");
                int n = 1;
                if(archivo.exists()){
                    n=(int)((archivo.length()))/9;
                    indice.seek(0);
                    for (int i = 0; i < n; i++) {
                        int llave,direccion;
                        boolean ban;
                        llave=indice.readInt();
                        direccion = indice.readInt();
                        ban = indice.readBoolean();
                        if(ban == true){
                            //System.out.println(llave);
                            arbol_indice.insertar(llave, direccion);
                        }
                    }
                }
                else{
                    //System.out.println("No existen registros en el archivo");
                }
            } catch (Exception e) {
                //System.out.println("error al crear un arbol");
                e.printStackTrace();
            }
        }
    }
    public void inicializar(){
        try{
            maestro = new RandomAccessFile("maestro.dat","rw");
            indice = new RandomAccessFile("indice.dat","rw");
            arbol_indice = new ArbolBinario();
            
            //arbol_indice.insert(0, 0);
            
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public ArrayList<Registro> leerTodos() {
        ArrayList<Registro> lista= new ArrayList<Registro>();
        Registro temp;
        try {
                File archivo = new File("maestro.dat");
                int n = 1;
                if(archivo.exists()){
                    n=(int)((archivo.length()))/2105;
                    maestro.seek(0);
                    for (int i = 0; i < n; i++) {
                        temp = new Registro();
                        temp.llave= maestro.readInt();
                        temp.Consecuente = leerCadena().replaceAll(" ", "");
                        for (int j = 0; j < 20; j++) {
                                String cadena = leerCadena();
                                String cade = cadena.replaceAll(" ", "");
                                if(!cade.isEmpty()){ //Solo muestra los que no estan vacios 
                                    temp.Antecedentes.add(cade);
                            }
                        }
                        temp.disponible = maestro.readBoolean();
                        if(temp.disponible)
                            lista.add(temp);
                    }
                }
                else{
                    //System.out.println("No existen registros en el archivo");
                }
            } catch (Exception e) {
                //System.out.println("Error al usar el archivo");
                e.printStackTrace();
            }
        return lista;
    }
}
