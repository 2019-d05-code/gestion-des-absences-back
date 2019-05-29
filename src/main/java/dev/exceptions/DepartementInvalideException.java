package dev.exceptions;

public class DepartementInvalideException extends RuntimeException {

	private static final long serialVersionUID = 7278121982757280138L;

	public static String message;

	public DepartementInvalideException(String msg) {
		message = msg;
	}
}
