package com.trs.bookstoreservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	public static final Logger HANDLER_LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler({BadRequestException.class})
	public ResponseEntity<Object> handleAdviceError(BadRequestException badReqEx, WebRequest req) {
		
		HANDLER_LOGGER.error("handleHttpServerError response body: {}", badReqEx.getLocalizedMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badReqEx.getLocalizedMessage());	
	}
	
	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest req) {
		
		HANDLER_LOGGER.error("handleAll", ex);
		HANDLER_LOGGER.error("handleAll", req);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getLocalizedMessage());	
	}
	
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
																Object body,
																HttpHeaders headers,
																HttpStatus status,
																WebRequest req) {
		HANDLER_LOGGER.error("handleExceptionInternal", ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getLocalizedMessage());	
	}
	
}
