package com.theelixrlabs.healthcare.exceptionHandler;

/**
 * Doctor exception thrown when a requested resource is not found.
 */
public class DoctorNotFound extends RuntimeException {
    /**
     * Constructs a new DoctorNotFound with the specified detail message
     *
     * @param message the detail message.
     */
    public DoctorNotFound(String message) {
        super(message);
    }
}
