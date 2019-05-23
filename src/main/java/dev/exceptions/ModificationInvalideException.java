package dev.exceptions;

public class ModificationInvalideException extends RuntimeException {

	private static final long serialVersionUID = 2601229493287384501L;
	
	public static String message;

	public ModificationInvalideException(String msg) {
		message = msg;
	}

}
