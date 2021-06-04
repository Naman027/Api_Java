package net.proj.springboot.exception;
import net.proj.springboot.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import javax.sql.rowset.serial.SerialException;

@ControllerAdvice // Annotation to control exceptions globally..
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class) // Handle the specific exception
    public ResponseEntity<ErrorDetails> ResourceNotFoundExceptionHandler(ResourceNotFoundException exception){
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(),"1404");
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ErrorDetails> GenericExceptionHandler(GenericException genericException){
        ErrorDetails errorDetails = new ErrorDetails(genericException.getMessage(),"1408");
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<?> NoSuchAlgorithmException(NoSuchAlgorithmException exception){
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(),"1405");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(ServletExceptionHandler.class)
    public ResponseEntity<?> ServletExceptionHandler(ServletException exception){
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(),"1405");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_GATEWAY);
    }

}
