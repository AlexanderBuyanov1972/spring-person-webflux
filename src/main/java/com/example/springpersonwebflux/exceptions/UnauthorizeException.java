package com.example.springpersonwebflux.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizeException extends ApiException {

    public UnauthorizeException(String message) {
        super(message, "UNAUTHORIZED");
    }

}
