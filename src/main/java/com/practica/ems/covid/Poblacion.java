package com.practica.ems.covid;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.practica.excecption.EmsDuplicatePersonException;
import com.practica.excecption.EmsPersonNotFoundException;
import com.practica.genericas.FechaHora;
import com.practica.genericas.Persona;


public class Poblacion {
	List<Persona> lista ;

	public Poblacion() {
		super();
		this.lista = new LinkedList<>();
	}
	
	public List<Persona> getLista() {
		return lista;
	}

	public void setLista(List<Persona> lista) {
		this.lista = lista;
	}

	public void addPersona (Persona persona) throws EmsDuplicatePersonException {
		try {
			findPersona(persona.getDocumento());
			throw new EmsDuplicatePersonException();
		} catch (EmsPersonNotFoundException e) {
			lista.add(persona);
		} 
	}
	
	public void delPersona(String documento) throws EmsPersonNotFoundException {
		int pos=-1;
		/**
		 * Busca la persona por documento, en caso de encontrarla
		 * devuelve la posición dentro de la lista, sino está lanza
		 * una excepción
		 */
		try {
			pos = findPersona(documento);
		} catch (EmsPersonNotFoundException e) {
			throw new EmsPersonNotFoundException();
		}
		lista.remove(pos);		
	}
	
	public int findPersona (String documento) throws EmsPersonNotFoundException {
		int cont=0;
		Iterator<Persona> it = lista.iterator();
		while (it.hasNext() ) {
			Persona persona = it.next();
			cont++;
			if(persona.getDocumento().equals(documento)) {
				return cont;
			}
		}		
		throw new EmsPersonNotFoundException();
	}
	
	public void printPoblacion() {
		Logger logger = Logger.getLogger(Poblacion.class.getName());

	    for(int i = 0; i < lista.size(); i++) {	    	
	    	FechaHora fecha = lista.get(i).getFechaNacimiento();
	        // Documento	    	
			logger.log(Level.ALL, String.format("%d;%s;", i, lista.get(i).getDocumento()));
	        // nombre y apellidos	              
			logger.log(Level.ALL, String.format("%s,%s;",lista.get(i).getApellidos(), lista.get(i).getNombre()));
	        // correo electrónico
			logger.log(Level.ALL, String.format("%s;", lista.get(i).getEmail()));
	        // Códifo postal
			logger.log(Level.ALL, String.format("%s,%s;", lista.get(i).getDireccion(), lista.get(i).getCp()));
	        // Fecha de nacimiento
			logger.log(Level.ALL, String.format("%02d/%02d/%04d%n", fecha.getFecha().getDia(),
	         fecha.getFecha().getMes(),
	         fecha.getFecha().getAnio()));
		}
	}

	@Override
	public String toString() {
		StringBuilder cadena = new StringBuilder();
		for (Persona persona : lista) {
			FechaHora fecha = persona.getFechaNacimiento();
			// Documento
			cadena.append(String.format("%s;", persona.getDocumento()));
			// nombre y apellidos
			cadena.append(String.format("%s,%s;", persona.getApellidos(), persona.getNombre()));
			// correo electrónico
			cadena.append(String.format("%s;", persona.getEmail()));
			// Direccion y código postal
			cadena.append(String.format("%s,%s;", persona.getDireccion(), persona.getCp()));
			// Fecha de nacimiento
			cadena.append(String.format("%02d/%02d/%04d%n", fecha.getFecha().getDia(),
					fecha.getFecha().getMes(),
					fecha.getFecha().getAnio()));
		}
		return cadena.toString();
	}
	
	
	
}
