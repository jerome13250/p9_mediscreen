package com.mediscreen.mnote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoteNotFoundException extends Exception {

	/**
	 * auto generated serial uid
	 */
	private static final long serialVersionUID = -802819922917095334L;

	
	public NoteNotFoundException(String message) {
		super(message);
	}

}