package com.dsp.agendaPapers.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.dsp.agendaPapers.response.GenericResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<GenericResponse<String>> handleResourceNotFoundException(ResourceNotFoundException ex,
			WebRequest request) {

		// Create a custom error response
		GenericResponse<String> errorResponse = new GenericResponse<>(false, ex.getMessage(), null, // No additional
																									// details
				HttpStatus.NOT_FOUND.value());

		// Return a ResponseEntity with the custom error message
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	// Handle other exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<GenericResponse<String>> handleGlobalException(Exception ex, WebRequest request) {

		// Log the exception for debugging
		ex.printStackTrace();

		// Create a generic error response
		GenericResponse<String> errorResponse = new GenericResponse<>(false,
				"An unexpected error occurred: " + ex.getMessage(), null, // No additional details
				HttpStatus.INTERNAL_SERVER_ERROR.value());

		// Return a ResponseEntity with the generic error message
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
