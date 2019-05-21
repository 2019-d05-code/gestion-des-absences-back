package dev.exceptions;

public class CollegueNonTrouveException extends RuntimeException {

	private static final long serialVersionUID = -7250391090304810151L;
	
	public static String message;
	
	public CollegueNonTrouveException(String msg) {
		message = msg;
	}
	
}
