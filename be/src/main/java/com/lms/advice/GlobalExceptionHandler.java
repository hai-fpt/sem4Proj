package com.lms.advice;

import com.lms.exception.NotFoundByIdException;
import com.lms.exception.UnauthorizedException;
import org.codehaus.groovy.syntax.TokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorMessage> handleNullPointerException(NullPointerException ex, WebRequest request) {
		ex.printStackTrace();
		ErrorMessage errorMessage = new ErrorMessage(
				HttpStatus.NOT_FOUND.value(),
				new Date(),
				ex.getMessage(),
				request.getDescription(false)
		);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorMessage> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
		ex.printStackTrace();
		ErrorMessage errorMessage = new ErrorMessage(
				HttpStatus.UNAUTHORIZED.value(),
				new Date(),
				ex.getMessage(),
				request.getDescription(false)
		);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
	}
	@ExceptionHandler(NotFoundByIdException.class)
	public ResponseEntity<ErrorMessage> handleNotFoundByIdException(NotFoundByIdException ex, WebRequest request) {
		ex.printStackTrace();
		ErrorMessage errorMessage = new ErrorMessage(
				HttpStatus.NOT_FOUND.value(),
				new Date(),
				ex.getMessage(),
				request.getDescription(false)
		);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
	}
}
