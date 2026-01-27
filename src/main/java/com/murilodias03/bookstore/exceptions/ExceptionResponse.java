package com.murilodias03.bookstore.exceptions;

import java.util.Date;

public record ExceptionResponse(
        Date timestamp,
        String message,
        String details
) {}