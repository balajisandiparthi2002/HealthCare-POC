package com.theelixrlabs.healthcare.exceptionHandler;

public class PatientException extends Exception{

    /**
     * Constructor to create a PatientException with a specific exception message.
     *
     * @param exceptionMessage The message describing the exception.
     */
    public PatientException(String exceptionMessage) {
        // Call the constructor of the superclass (Exception) with the provided exception message
        super(exceptionMessage);
    }
}
