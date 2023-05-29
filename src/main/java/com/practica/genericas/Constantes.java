package com.practica.genericas;


public enum Constantes {
	MAX_DATOS_PERSONA(8),
	MAX_DATOS_LOCALIZACION(6);

	public final int valor;

	Constantes(int valor) {
		this.valor = valor;
	}
}
