package com.practica.excecption;


public class EmsDuplicateLocationException extends Exception{
	public EmsDuplicateLocationException() {
		super("LOCALIZACION DUPLICADA!");
	}

	public EmsDuplicateLocationException(String message) {
		super(message);
	}
}
