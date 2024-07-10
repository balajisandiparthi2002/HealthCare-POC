package com.theelixrlabs.healthcare.exceptionHandler;

import org.springframework.context.MessageSource;

/**
 * Custom exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructs a new ResourceNotFoundException with the specified detail message
     *
     * @param message the detail message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with a message sourced from the provided MessageSource.
     *
     * @param messageKey    the key for the message in the MessageSource.
     * @param messageSource the MessageSource to look up the message.
     */
    public ResourceNotFoundException(String messageKey, MessageSource messageSource) {
        super(messageSource.getMessage(messageKey, null, null));
    }
}
