package com.amit.utility;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.amit.exception.EKartException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

	@Autowired
	Environment environment;
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo> generalExceptionHandler(Exception exception){
		ErrorInfo error = new ErrorInfo();
		error.setErrorMessage(environment.getProperty("General.EXCEPTION_MESSAGE")+exception.getMessage());
		error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setTimeStamp(LocalDateTime.now());
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EKartException.class)
	public ResponseEntity<ErrorInfo> ekartExceptionHandler(EKartException exception){
		ErrorInfo error = new ErrorInfo();
		error.setErrorMessage(environment.getProperty(exception.getMessage()));
		error.setErrorCode(HttpStatus.BAD_REQUEST.value());
		error.setTimeStamp(LocalDateTime.now());
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
	public ResponseEntity<ErrorInfo> exceptionHandler(Exception exception){
		ErrorInfo error = new ErrorInfo();
		error.setErrorCode(HttpStatus.BAD_REQUEST.value());
		String errorMsg= "";
		if(exception instanceof MethodArgumentNotValidException) {
			
			MethodArgumentNotValidException exceptional=(MethodArgumentNotValidException) exception;
			errorMsg = exceptional.getBindingResult().getAllErrors().stream().map(x->x.getDefaultMessage())
					.collect(Collectors.joining(", "));
		}else {
			ConstraintViolationException exceptional = (ConstraintViolationException) exception;
			errorMsg = exceptional.getConstraintViolations().stream().map(x->x.getMessage())
					.collect(Collectors.joining(", "));
		}
		error.setErrorMessage(errorMsg);
		error.setTimeStamp(LocalDateTime.now());
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
}
