package com.practica.excecption;


public class EmsInvalidNumberOfDataException extends Exception {

	public EmsInvalidNumberOfDataException() {
		super("NUMERO DE PARAMETROS INCORRECTOS!");
	}

	public EmsInvalidNumberOfDataException(String message) {
		super(message);
	}
	
}
