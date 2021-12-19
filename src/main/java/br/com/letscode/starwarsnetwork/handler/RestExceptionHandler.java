package br.com.letscode.starwarsnetwork.handler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.letscode.starwarsnetwork.error.ErrorDetails;
import br.com.letscode.starwarsnetwork.error.ResourceNotFoundDetails;
import br.com.letscode.starwarsnetwork.error.ResourceNotFoundException;
import br.com.letscode.starwarsnetwork.error.ValidationErrorDetails;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rnfException){
		ResourceNotFoundDetails rnfDetails =  ResourceNotFoundDetails.Builder
			.newBuilder()
			.timestamp(new Date().getTime())
			.status(HttpStatus.NOT_FOUND.value())
			.title("Resource not found")
			.detail(rnfException.getMessage())
			.developerMessage(rnfException.getClass().getName())
			.build();
		
		return new ResponseEntity<>(rnfDetails,HttpStatus.NOT_FOUND);		
	}
	
	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manvException, 
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> errors = manvException.getBindingResult().getFieldErrors();
		String fields = errors.stream().map(FieldError::getField).collect(Collectors.joining(";"));
		String fildsMessages = errors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(";"));
		ValidationErrorDetails rnfDetails =  ValidationErrorDetails.Builder
			.newBuilder()
			.timestamp(new Date().getTime())
			.status(HttpStatus.BAD_REQUEST.value())
			.title("Field Validation error")
			.detail("Field Validation error")
			.developerMessage(manvException.getClass().getName())
			.field(fields)
			.fieldMessage(fildsMessages)
			.build();
		
		return new ResponseEntity<>(rnfDetails,HttpStatus.NOT_FOUND);		
	}
	
//	@Override
//	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//		ErrorDetails errorDetails =  ErrorDetails.Builder
//				.newBuilder()
//				.timestamp(new Date().getTime())
//				.status(HttpStatus.NOT_FOUND.value())
//				.title("Resource not found")
//				.detail(ex.getMessage())
//				.developerMessage(ex.getClass().getName())
//				.build();
//		
//		return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
//	}
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorDetails errorDetails =  ErrorDetails.Builder
				.newBuilder()
				.timestamp(new Date().getTime())
				.status(status.value())
				.title("Internal Exception")
				.detail(ex.getMessage())
				.developerMessage(ex.getClass().getName())
				.build();
		return new ResponseEntity<>(errorDetails, headers, status);
	}
	
	
}
