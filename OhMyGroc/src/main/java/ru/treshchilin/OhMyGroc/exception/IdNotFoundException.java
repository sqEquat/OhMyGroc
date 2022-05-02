package ru.treshchilin.OhMyGroc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class IdNotFoundException extends RuntimeException{

	public IdNotFoundException(String message) {
		super(message);
	}
}
