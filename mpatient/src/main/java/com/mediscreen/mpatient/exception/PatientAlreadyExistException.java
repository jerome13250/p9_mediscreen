package com.mediscreen.mpatient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PatientAlreadyExistException extends Exception {

	/**
	 * auto generated serial uid
	 */
	private static final long serialVersionUID = 8899559872585314596L;

	public PatientAlreadyExistException(String message) {
        super(message);
    }
	
}
