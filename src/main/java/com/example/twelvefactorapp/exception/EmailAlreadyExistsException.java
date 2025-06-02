package com.example.twelvefactorapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Consider a more specific HTTP status if this exception is directly handled by Spring MVC's
// ResponseEntityExceptionHandler, e.g., HttpStatus.CONFLICT (409)
@ResponseStatus(HttpStatus.BAD_REQUEST) // Or HttpStatus.CONFLICT
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
