package com.theelixrlabs.healthcare.exceptionHandler;

import com.theelixrlabs.healthcare.response.FailureResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler to handling exceptions thrown by REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Exception handler for MethodArgumentNotValidException.
     *
     * @param methodArgumentNotValidException MethodArgumentNotValidException exception instance.
     * @return ResponseEntity failure response with error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailureResponse> handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult errorResults = methodArgumentNotValidException.getBindingResult();
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : errorResults.getFieldErrors()) {
            errorMessages.add(fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(new FailureResponse(false, errorMessages), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for custom exceptions and validations.
     *
     * @param customException customException instance thrown during runtime.
     * @return ResponseEntity failure response with error messages.
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<FailureResponse> handleExistingData(CustomException customException) {
        List<String> errors = new ArrayList<>();
        errors.add(customException.getMessage());
        return new ResponseEntity<>(new FailureResponse(false, errors), HttpStatus.BAD_REQUEST);
    }
}
