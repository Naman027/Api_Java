package net.proj.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchAlgorithmException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public NoSuchAlgorithmException(String message){
        super(message);
    }
}
