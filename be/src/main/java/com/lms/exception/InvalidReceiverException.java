package com.lms.exception;


public class InvalidReceiverException extends Exception {

	public InvalidReceiverException(String message) {
		super(message);
	}
	public InvalidReceiverException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
