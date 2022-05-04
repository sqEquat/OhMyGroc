package ru.treshchilin.OhMyGroc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class IdNotFoundException extends RuntimeException{

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -3626624899959123899L;

	public IdNotFoundException(String message) {
		super(message);
	}
}
