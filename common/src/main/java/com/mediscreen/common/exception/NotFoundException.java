package com.mediscreen.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends Exception {

	/**
	 * auto generated serial uid
	 */
	private static final long serialVersionUID = 5446457385777126116L;

	public NotFoundException(String message) {
		super(message);
	}

}