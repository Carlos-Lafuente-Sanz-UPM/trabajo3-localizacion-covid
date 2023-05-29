package com.practica.ems.covid;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Principal {


	public static void main(String[] args) {
		Logger logger = Logger.getLogger(Principal.class.getName());

		ContactosCovid contactosCovid = new ContactosCovid();
		contactosCovid.loadDataFile("datos2.txt", false);
		logger.log(Level.ALL, contactosCovid.getLocalizacion().toString());
		logger.log(Level.ALL, contactosCovid.getPoblacion().toString());
		logger.log(Level.ALL, "" + contactosCovid.getListaContactos().tamanioLista());
		logger.log(Level.ALL, contactosCovid.getListaContactos().getPrimerNodo());
		logger.log(Level.ALL, contactosCovid.getListaContactos().toString());
	}
}
