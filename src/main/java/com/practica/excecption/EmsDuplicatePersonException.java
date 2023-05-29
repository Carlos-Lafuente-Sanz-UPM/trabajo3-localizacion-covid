package com.practica.excecption;


public class EmsDuplicatePersonException extends Exception{
	public EmsDuplicatePersonException() {
		super("PERSONA DUPLICADA!");
	}

	public EmsDuplicatePersonException(String message) {
		super(message);
	}
}
