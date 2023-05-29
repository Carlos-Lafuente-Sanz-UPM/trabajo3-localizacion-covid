package com.practica.ems.covid;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.practica.excecption.EmsDuplicateLocationException;
import com.practica.excecption.EmsLocalizationNotFoundException;
import com.practica.genericas.FechaHora;
import com.practica.genericas.PosicionPersona;


public class Localizacion {
	List<PosicionPersona> lista;

	public Localizacion() {
		super();
		this.lista = new LinkedList<>();
	}

	public List<PosicionPersona> getLista() {
		return lista;
	}

	public void setLista(List<PosicionPersona> lista) {
		this.lista = lista;
	}

	public void addLocalizacion (PosicionPersona p) throws EmsDuplicateLocationException {
		try {
			findLocalizacion(p.getDocumento(), p.getFechaPosicion().getFecha().toString(),p.getFechaPosicion().getHora().toString() );
			throw new EmsDuplicateLocationException();
		}catch(EmsLocalizationNotFoundException e) {
			lista.add(p);
		}
	}
	
	public int findLocalizacion (String documento, String fecha, String hora) throws EmsLocalizationNotFoundException {
	    int cont = 0;
	    Iterator<PosicionPersona> it = lista.iterator();
	    while(it.hasNext()) {
	    	cont++;
	    	PosicionPersona pp = it.next();
	    	FechaHora fechaHora = this.parsearFecha(fecha, hora);
	    	if(pp.getDocumento().equals(documento) && 
	    	   pp.getFechaPosicion().equals(fechaHora)) {
	    		return cont;
	    	}
	    } 
	    throw new EmsLocalizationNotFoundException();
	}
	public void delLocalizacion(String documento, String fecha, String hora) throws EmsLocalizationNotFoundException {
	    int pos=-1;
	    /**
	     *  Busca la localización, sino existe lanza una excepción
	     */
	    try {
			pos = findLocalizacion(documento, fecha, hora);
		} catch (EmsLocalizationNotFoundException e) {
			throw new EmsLocalizationNotFoundException();
		}
	    this.lista.remove(pos);
	    
	}
	
	void printLocalizacion() {
		Logger logger = Logger.getLogger(Localizacion.class.getName());

	    for(int i = 0; i < this.lista.size(); i++) {
			logger.log(Level.ALL, String.format("%d;%s;", i, lista.get(i).getDocumento()));
	        FechaHora fecha = lista.get(i).getFechaPosicion();
			logger.log(Level.ALL, String.format("%02d/%02d/%04d;%02d:%02d;",
	        		fecha.getFecha().getDia(), 
	        		fecha.getFecha().getMes(), 
	        		fecha.getFecha().getAnio(),
	        		fecha.getHora().getHoraJessica(),
	        		fecha.getHora().getMinuto()));
			logger.log(Level.ALL, String.format("%.4f;%.4f%n", lista.get(i).getCoordenada().getLatitud(),
	        		lista.get(i).getCoordenada().getLongitud()));
	    }
	}

	@Override
	public String toString() {
		StringBuilder cadena = new StringBuilder();
		for (PosicionPersona pp : this.lista) {
			cadena.append(String.format("%s;", pp.getDocumento()));
			FechaHora fecha = pp.getFechaPosicion();
			cadena.append(String.format("%02d/%02d/%04d;%02d:%02d;",
					fecha.getFecha().getDia(),
					fecha.getFecha().getMes(),
					fecha.getFecha().getAnio(),
					fecha.getHora().getHoraJessica(),
					fecha.getHora().getMinuto()));
			cadena.append(String.format("%.4f;%.4f%n", pp.getCoordenada().getLatitud(),
					pp.getCoordenada().getLongitud()));
		}
		
		return cadena.toString();
	}
	
	@SuppressWarnings("unused")
	private FechaHora parsearFecha (String fecha) {
		int dia;
		int mes;
		int anio;
		String[] valores = fecha.split("\\/");
		dia = Integer.parseInt(valores[0]);
		mes = Integer.parseInt(valores[1]);
		anio = Integer.parseInt(valores[2]);
		return new FechaHora(dia, mes, anio, 0, 0);
	}
	
	private  FechaHora parsearFecha (String fecha, String hora) {
		int dia;
		int mes;
		int anio;
		String[] valores = fecha.split("\\/");
		dia = Integer.parseInt(valores[0]);
		mes = Integer.parseInt(valores[1]);
		anio = Integer.parseInt(valores[2]);
		int minuto;
		int segundo;
		valores = hora.split("\\:");
		minuto = Integer.parseInt(valores[0]);
		segundo = Integer.parseInt(valores[1]);
		return new FechaHora(dia, mes, anio, minuto, segundo);
	}
	
}
