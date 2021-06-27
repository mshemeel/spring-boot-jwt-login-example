package com.example.spring.security.postgresql.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.spring.security.postgresql.payload.response.MessageResponse;
/*This is the common exception handler for all the controllers*/
@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler{

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	  public ResponseEntity<MessageResponse> handleMaxSizeException(MaxUploadSizeExceededException exc) {
	    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("File too large!"));
	  }
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<MessageResponse> handleRunTimeException(RuntimeException exc) {
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(exc.getLocalizedMessage()));
	  } 
	
}
