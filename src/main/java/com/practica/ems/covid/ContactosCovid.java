package com.practica.ems.covid;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.practica.excecption.EmsDuplicateLocationException;
import com.practica.excecption.EmsDuplicatePersonException;
import com.practica.excecption.EmsInvalidNumberOfDataException;
import com.practica.excecption.EmsInvalidTypeException;
import com.practica.excecption.EmsLocalizationNotFoundException;
import com.practica.excecption.EmsPersonNotFoundException;
import com.practica.genericas.Constantes;
import com.practica.genericas.Coordenada;
import com.practica.genericas.FechaHora;
import com.practica.genericas.Persona;
import com.practica.genericas.PosicionPersona;
import com.practica.lista.ListaContactos;

public class ContactosCovid {
	private Poblacion poblacion;
	private Localizacion localizacion;
	private ListaContactos listaContactos;
	private final String personaString = "PERSONA";
	private final String localizacionString = "LOCALIZACION";

	public ContactosCovid() {
		this.poblacion = new Poblacion();
		this.localizacion = new Localizacion();
		this.listaContactos = new ListaContactos();
	}

	public Poblacion getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(Poblacion poblacion) {
		this.poblacion = poblacion;
	}

	public Localizacion getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(Localizacion localizacion) {
		this.localizacion = localizacion;
	}
	
	

	public ListaContactos getListaContactos() {
		return listaContactos;
	}

	public void setListaContactos(ListaContactos listaContactos) {
		this.listaContactos = listaContactos;
	}

	public void loadData(String data, boolean reset) throws EmsInvalidTypeException, EmsInvalidNumberOfDataException,
			EmsDuplicatePersonException, EmsDuplicateLocationException {
		if (reset) {
			this.poblacion = new Poblacion();
			this.localizacion = new Localizacion();
			this.listaContactos = new ListaContactos();
		}
		String[] datas = dividirEntrada(data);
		for (String linea : datas) {
			String[] datos = this.dividirLineaData(linea);
			if (!datos[0].equals(personaString) && !datos[0].equals(localizacionString)) {
				throw new EmsInvalidTypeException();
			}
			if (datos[0].equals(personaString)) {
				if (datos.length != Constantes.MAX_DATOS_PERSONA.valor) {
					throw new EmsInvalidNumberOfDataException("El número de datos para PERSONA es menor de 8");
				}
				this.poblacion.addPersona(this.crearPersona(datos));
			}
			if (datos[0].equals(localizacionString)) {
				if (datos.length != Constantes.MAX_DATOS_LOCALIZACION.valor) {
					throw new EmsInvalidNumberOfDataException("El número de datos para LOCALIZACION es menor de 6");
				}
				PosicionPersona pp = this.crearPosicionPersona(datos);
				this.localizacion.addLocalizacion(pp);
				this.listaContactos.insertarNodoTemporal(pp);
			}
		}
	}

	private void loadDatosAux(String[] datos) throws EmsInvalidNumberOfDataException, EmsDuplicatePersonException, EmsDuplicateLocationException, EmsInvalidTypeException {
		if (datos[0].equals(personaString)) {
			if (datos.length != Constantes.MAX_DATOS_PERSONA.valor) {
				throw new EmsInvalidNumberOfDataException("El número de datos para PERSONA es menor de 8");
			}
			this.poblacion.addPersona(this.crearPersona(datos));
		}
		else if (datos[0].equals(localizacionString)) {
			if (datos.length != Constantes.MAX_DATOS_LOCALIZACION.valor) {
				throw new EmsInvalidNumberOfDataException(
						"El número de datos para LOCALIZACION es menor de 6" );
			}
			PosicionPersona pp = this.crearPosicionPersona(datos);
			this.localizacion.addLocalizacion(pp);
			this.listaContactos.insertarNodoTemporal(pp);
		}
		else
			throw new EmsInvalidTypeException();
	}

	@SuppressWarnings("resource")
	public void loadDataFile(String fichero, boolean reset) {
		try {
			File archivo = new File(fichero);
			FileReader fr = new FileReader(archivo);
			BufferedReader br = new BufferedReader(fr);
			String data = null;
			if (reset) {
				this.poblacion = new Poblacion();
				this.localizacion = new Localizacion();
				this.listaContactos = new ListaContactos();
			}
			while ((data = br.readLine()) != null) {
				String[] datas = dividirEntrada(data.trim());
				for (String linea : datas) {
					String[] datos = this.dividirLineaData(linea);
					loadDatosAux(datos);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				FileReader fr = new FileReader(new File(fichero));
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	public int findPersona(String documento) throws EmsPersonNotFoundException {
		int pos;
		try {
			pos = this.poblacion.findPersona(documento);
			return pos;
		} catch (EmsPersonNotFoundException e) {
			throw new EmsPersonNotFoundException();
		}
	}

	public int findLocalizacion(String documento, String fecha, String hora) throws EmsLocalizationNotFoundException {

		int pos;
		try {
			pos = localizacion.findLocalizacion(documento, fecha, hora);
			return pos;
		} catch (EmsLocalizationNotFoundException e) {
			throw new EmsLocalizationNotFoundException();
		}
	}

	public List<PosicionPersona> localizacionPersona(String documento) throws EmsPersonNotFoundException {
		int cont = 0;
		List<PosicionPersona> lista = new ArrayList<>();
		Iterator<PosicionPersona> it = this.localizacion.getLista().iterator();
		while (it.hasNext()) {
			PosicionPersona pp = it.next();
			if (pp.getDocumento().equals(documento)) {
				cont++;
				lista.add(pp);
			}
		}
		if (cont == 0)
			throw new EmsPersonNotFoundException();
		else
			return lista;
	}

	public boolean delPersona(String documento) throws EmsPersonNotFoundException {
		int cont = 0;
		int pos = -1;
		Iterator<Persona> it = this.poblacion.getLista().iterator();
		while (it.hasNext()) {
			Persona persona = it.next();
			if (persona.getDocumento().equals(documento)) {
				pos = cont;
			}
			cont++;
		}
		if (pos == -1) {
			throw new EmsPersonNotFoundException();
		}
		this.poblacion.getLista().remove(pos);
		return false;
	}

	private String[] dividirEntrada(String input) {
		return input.split("\\n");
	}

	private String[] dividirLineaData(String data) {
		return data.split("\\;");
	}

	private Persona crearPersona(String[] data) {
		Persona persona = new Persona();
		for (int i = 1; i < Constantes.MAX_DATOS_PERSONA.valor; i++) {
			String s = data[i];
			switch (i) {
			case 1:
				persona.setDocumento(s);
				break;
			case 2:
				persona.setNombre(s);
				break;
			case 3:
				persona.setApellidos(s);
				break;
			case 4:
				persona.setEmail(s);
				break;
			case 5:
				persona.setDireccion(s);
				break;
			case 6:
				persona.setCp(s);
				break;
			default:
				persona.setFechaNacimiento(parsearFecha(s));
				break;
			}
		}
		return persona;
	}

	private PosicionPersona crearPosicionPersona(String[] data) throws NullPointerException {
		PosicionPersona posicionPersona = new PosicionPersona();
		String fecha = data[0];
		String hora;
		float latitud = 0;
		float longitud;
		for (int i = 1; i < Constantes.MAX_DATOS_LOCALIZACION.valor; i++) {
			String s = data[i];
			switch (i) {
			case 1:
				posicionPersona.setDocumento(s);
				break;
			case 2:
				fecha = data[i];
				break;
			case 3:
				hora = data[i];
				posicionPersona.setFechaPosicion(parsearFecha(fecha, hora));
				break;
			case 4:
				latitud = Float.parseFloat(s);
				break;
			default:
				longitud = Float.parseFloat(s);
				posicionPersona.setCoordenada(new Coordenada(latitud, longitud));
				break;
			}
		}
		return posicionPersona;
	}
	
	private FechaHora parsearFecha (String fecha) throws NullPointerException {
		int dia;
		int mes;
		int anio;
		String[] valores = fecha.split("\\/");
		dia = Integer.parseInt(valores[0]);
		mes = Integer.parseInt(valores[1]);
		anio = Integer.parseInt(valores[2]);
		return new FechaHora(dia, mes, anio, 0, 0);
	}
	
	private FechaHora parsearFecha (String fecha, String hora) {
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
