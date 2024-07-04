package com.theelixrlabs.healthcare.exceptionHandler;

import org.springframework.context.MessageSource;

/**
 * Custom Exception class for handling exception.
 */
public class CustomException extends RuntimeException {
    public CustomException(String exceptionMessage) {
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
