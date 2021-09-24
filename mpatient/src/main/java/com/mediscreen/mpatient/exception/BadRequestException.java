package com.mediscreen.mpatient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {

	/**
	 * auto generated serial uid
	 */
	private static final long serialVersionUID = -8695875255906289481L;

	public BadRequestException(String message) {
        super(message);
    }
	
}
