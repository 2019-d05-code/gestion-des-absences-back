package dev.exceptions;

public class DemandeNonTrouveException extends RuntimeException {
	
	private static final long serialVersionUID = -4974714542723622100L;
	
	public static String message;

	public DemandeNonTrouveException(String msg) {
		message = msg;
	}

}
