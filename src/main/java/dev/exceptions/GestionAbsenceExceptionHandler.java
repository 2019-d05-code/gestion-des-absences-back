package dev.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


/**
 * Gestion des exceptions en cas d'appel des services depuis le controlleur
 * 
 * @author Nicolas
 *
 */
@ControllerAdvice
public class GestionAbsenceExceptionHandler {

	@ExceptionHandler(value = { DemandeInvalideException.class })
	protected ResponseEntity<Object> handleConflictDemandeInvalide(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "Demande invalide: " + DemandeInvalideException.message;
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bodyOfResponse);
	 }
	
	@ExceptionHandler(value = { CollegueNonTrouveException.class })
	protected ResponseEntity<Object> handleConflictCollegueNonTrouve(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "Collegue non trouvé invalide: " + CollegueNonTrouveException.message;
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bodyOfResponse);
	 }
	
}
