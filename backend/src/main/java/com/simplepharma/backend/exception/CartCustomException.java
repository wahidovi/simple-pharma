package com.simplepharma.backend.exception;

public class CartCustomException extends RuntimeException {

	private static final long serialVersionUID = 2325949205764962693L;

	public CartCustomException(String message) {
		super(message);
	}

}