package com.theelixrlabs.healthcare.exceptionhandler;

import com.theelixrlabs.healthcare.response.FailureResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler to handle exceptions thrown by controller and service layers.
 * Validates input parameters based on the specified annotations.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles MethodArgumentNotValidException thrown when input parameters fail validation.
     *
     * @param methodArgumentNotValidException    The exception instance thrown during validation.
     * @return ResponseEntity with FailureResponse containing validation error message and HTTP status BAD_REQUEST.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailureResponse> handleInvalidArgument(MethodArgumentNotValidException methodArgumentNotValidException) {
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            errors.add(fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(new FailureResponse(false, errors), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles RuntimeExceptions thrown by the application.
     *
     * @param customException    The runtime exception instance.
     * @return ResponseEntity with FailureResponse containing the runtime exception message.
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<FailureResponse> handleExistingData(CustomException customException) {
        List<String> errors = new ArrayList<>();
        errors.add(customException.getMessage());
        return new ResponseEntity<>(new FailureResponse(false, errors), HttpStatus.BAD_REQUEST);
    }
}
