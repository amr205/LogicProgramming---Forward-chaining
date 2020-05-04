/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototipo1;

/**
 *
 * @author apaulina_mc
 */
class ArbolBinario { 

	/* Clase que contiene hijo izquierdo y derecho del nodo actual y el valor de la llave*/
	class Nodo { 
		int llave; 
		int direccion; 
		Nodo izquierda; 
                Nodo derecha; 

		public Nodo(int item, int item2) { 
			llave = item; 
                        direccion = item2;
			izquierda = derecha = null; 
		} 
	} 

	Nodo raiz; 

	// Constructor 
	ArbolBinario() { 
		raiz = null; 
	} 

	void insertar(int llave,int direc) { 
            raiz = insertarLlave(raiz, llave,direc); 
	} 
	
	Nodo insertarLlave(Nodo raiz, int llave, int direc) { 

		/* Si el valor está vacío crea un nuevo nodo */
		if (raiz == null) { 
			raiz = new Nodo(llave,direc); 
			return raiz; 
		} 

		/* Si no, recorre el árbol hacia abajo */
		if (llave < raiz.llave) 
			raiz.izquierda = insertarLlave(raiz.izquierda, llave,direc); 
		else if (llave > raiz.llave) 
			raiz.derecha = insertarLlave(raiz.derecha, llave,direc); 

		/* devuelve el puntero del nodo */
		return raiz; 
	} 


        // Busca la llave en el árbol que recibe  
        public Nodo buscar(Nodo raiz, int llave) 
        { 
                // raiz esta vacía o raíz corresponde a la llave que se esta buscando 
           
                if (raiz==null || raiz.llave==llave) 
                        return raiz; 

                // Si es mayor el valor de la llave de la raiz 
                if (raiz.llave > llave) 
                        return buscar(raiz.izquierda, llave); 

                // Si es menor el valor de la llave de la raíz
                return buscar(raiz.derecha, llave); 
        } 

} 