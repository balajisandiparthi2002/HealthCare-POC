package com.theelixrlabs.healthcare.exceptionHandler;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.response.FailureResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Global exception handler to handling exceptions thrown by REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Exception handler for MethodArgumentNotValidException.
     *
     * @param exception : MethodArgumentNotValidException exception
     * @return ResponseEntity failure response with error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailureResponse> handleValidationException(MethodArgumentNotValidException exception) {
        BindingResult errorResults = exception.getBindingResult();
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : errorResults.getFieldErrors()) {
            errorMessages.add(fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(new FailureResponse(false, errorMessages), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for custom exceptions and validations.
     *
     * @param exception : exception instance thrown during runtime.
     * @return ResponseEntity failure response with error messages.
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<FailureResponse> handleAadhaarAlreadyPresentException(CustomException exception) {
        List<String> errorMessages = Arrays.asList(exception.getMessage().split(DoctorConstants.COMMA_DELIMITER));
        return new ResponseEntity<>(new FailureResponse(false, errorMessages), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<FailureResponse> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException) {
        List<String> errorMessagesList = Collections.singletonList(resourceNotFoundException.getMessage());
        return new ResponseEntity<>(new FailureResponse(false, errorMessagesList), HttpStatus.NOT_FOUND);
    }
}
