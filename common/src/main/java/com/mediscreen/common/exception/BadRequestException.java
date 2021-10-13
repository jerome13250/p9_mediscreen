package com.mediscreen.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {

	/**
	 * auto generated serial uid
	 */
	private static final long serialVersionUID = 7021820017692803760L;

	public BadRequestException(String message) {
        super(message);
    }
	
}
