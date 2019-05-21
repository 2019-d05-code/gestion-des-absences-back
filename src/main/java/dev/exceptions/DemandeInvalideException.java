package dev.exceptions;

public class DemandeInvalideException extends RuntimeException {

	private static final long serialVersionUID = 7136203765953027236L;
	
	public static String message;
	
	public DemandeInvalideException(String msg) {
		message = msg;
	}
	
}
