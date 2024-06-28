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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> errorMessages = errorResults.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(new FailureResponse(false,errorMessages), HttpStatus.BAD_REQUEST);
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
        return new ResponseEntity<>(new FailureResponse(false,errorMessages), HttpStatus.BAD_REQUEST);
    }


}
