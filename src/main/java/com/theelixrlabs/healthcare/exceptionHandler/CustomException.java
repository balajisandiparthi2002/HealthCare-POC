package com.theelixrlabs.healthcare.exceptionHandler;

/**
 * Custom Exception class for handling exception.
 */
public class CustomException extends RuntimeException {

    /**
     * Constructor to create a CustomException with a specific exception message.
     *
     * @param exceptionMessage The message describing the exception.
     */
    public CustomException(String exceptionMessage) {
        // Call the constructor of the superclass (Exception) with the provided exception message
        super(exceptionMessage);
    }
}
