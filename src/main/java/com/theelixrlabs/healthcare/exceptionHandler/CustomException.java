package com.theelixrlabs.healthcare.exceptionHandler;

/**
 * Custom Exception class for handling exception.
 */
public class CustomException extends RuntimeException {
    public CustomException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
