package net.proj.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Annotation to control exceptions globally..
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class) // Handle the specific exception
    public ResponseEntity<ErrorDetails> ResourceNotFoundExceptionHandler(ResourceNotFoundException exception){
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage());
        return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class) // Handle the specific exception
    public ResponseEntity<ErrorDetails> ExceptionHandler(Exception exception){
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage());
        return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public String NoSuchAlgorithmException(NoSuchAlgorithmException exception){
        return exception.getMessage();
    }

}
