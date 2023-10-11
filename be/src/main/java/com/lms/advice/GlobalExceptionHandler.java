package com.lms.advice;

import com.lms.exception.DuplicateException;
import com.lms.exception.NotFoundByIdException;
import com.lms.exception.UnauthorizedException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Date;

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

	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity<ErrorMessage> handleDuplicateException(DuplicateException ex, WebRequest request) {
		ex.printStackTrace();
		ErrorMessage errorMessage = new ErrorMessage(
				HttpStatus.BAD_REQUEST.value(),
				new Date(),
				ex.getMessage(),
				request.getDescription(false)
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	}
	@ExceptionHandler(FileSizeLimitExceededException.class)
	public ResponseEntity<ErrorMessage> handleSizeLimitExceededException(FileSizeLimitExceededException ex, WebRequest request) {
		ex.printStackTrace();
		ErrorMessage errorMessage = new ErrorMessage(
				HttpStatus.EXPECTATION_FAILED.value(),
				new Date(), ex.getMessage(),
				request.getDescription(false));
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(errorMessage);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorMessage> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, WebRequest request) {
		ex.printStackTrace();
		ErrorMessage errorMessage = new ErrorMessage(
				HttpStatus.EXPECTATION_FAILED.value(),
				new Date(),
				ex.getMessage(),
				request.getDescription(false));
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(errorMessage);
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<ErrorMessage> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
		ex.printStackTrace();
		ErrorMessage errorMessage = new ErrorMessage(
				HttpStatus.EXPECTATION_FAILED.value(),
				new Date(),
				"Data not found, action has failed.",
				request.getDescription(false));
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(errorMessage);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> handleOtherException(Exception ex, WebRequest request) {
		ex.printStackTrace();
		ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				new Date(),
				ex.getMessage(),
				request.getDescription(false));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
	}

}
