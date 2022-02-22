package com.jvdw.product.api;

import com.jvdw.product.api.dto.RestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * GlobalErrorHandler is the application's global exception handler.
 * This saves on repetitive try/catch blocks in controllers
 */
@ControllerAdvice
public class GlobalErrorHandler
{
    /**
     * Handle all exceptions
     * @return ResponseEntity a wrapper object including the response data, message, and status code
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestDto> handleError()
    {
        // inform the client of the error without providing details of the error
        return new ResponseEntity<>(new RestDto(null, "Internal error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
