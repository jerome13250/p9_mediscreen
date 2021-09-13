package com.mediscreen.mpatient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientNotFoundException extends Exception {

	/**
	 * auto generated serial uid
	 */
	private static final long serialVersionUID = -8507203880777656769L;

	public PatientNotFoundException(String message) {
        super(message);
    }
	
}
