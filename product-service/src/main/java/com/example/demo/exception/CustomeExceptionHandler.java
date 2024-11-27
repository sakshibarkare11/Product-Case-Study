package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@ControllerAdvice
public class CustomeExceptionHandler {

	private static final Logger logger=LoggerFactory.getLogger(CustomeExceptionHandler.class);
	
	@ExceptionHandler(value = {ProductNotFoundException.class})
	public ResponseEntity<Object> productNotFound(ProductNotFoundException ex)
	{
		logger.info(ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	@ExceptionHandler(value = {Exception.class})
	public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
		
		logger.info(ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
}
