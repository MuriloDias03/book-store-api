package com.murilodias03.bookstore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequeriedObjectsIsNullException extends RuntimeException {

    public RequeriedObjectsIsNullException() {
        super("It is not allowed to persist a null object!");
    }

    public RequeriedObjectsIsNullException(String message) {
        super(message);
    }
}
