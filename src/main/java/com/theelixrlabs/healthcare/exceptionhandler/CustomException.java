package com.theelixrlabs.healthcare.exceptionhandler;

import org.springframework.context.MessageSource;

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

    /**
     * Constructor to create a CustomException with a message from messages.properties file
     *
     * @param messageKey    Key for a corresponding message value in messages.properties files
     * @param messageSource message source object
     */
    public CustomException(String messageKey, MessageSource messageSource) {
        super(messageSource.getMessage(messageKey, null, null));
    }
}
