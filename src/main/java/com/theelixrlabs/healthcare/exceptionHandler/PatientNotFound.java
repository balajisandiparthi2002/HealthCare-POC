package com.theelixrlabs.healthcare.exceptionHandler;

/**
 * Patient exception thrown when a requested resource is not found.
 */
public class PatientNotFound extends RuntimeException {
    /**
     * Constructs a new PatientNotFound with the specified detail message
     *
     * @param message the detail message.
     */
    public PatientNotFound(String message) {
        super(message);
    }
}
