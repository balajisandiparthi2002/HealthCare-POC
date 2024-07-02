package com.theelixrlabs.healthcare.exceptionhandler;

/**
 * Custom exception class for handling required exceptions.
 * Extends Java's built-in Exception class.
 */
public class CustomException extends Exception {
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
