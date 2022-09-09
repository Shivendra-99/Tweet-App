package com.tweetapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(IncorrectLoginInfo.class)
	public ResponseEntity<?> InCorrectLoginInfo(IncorrectLoginInfo incorrect){
		return new ResponseEntity<>(incorrect.getMessage(),HttpStatus.BAD_REQUEST);
	}

}
