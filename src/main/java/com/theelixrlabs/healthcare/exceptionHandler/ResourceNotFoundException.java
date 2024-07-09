package com.theelixrlabs.healthcare.exceptionHandler;

import org.springframework.context.MessageSource;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String messageKey, MessageSource messageSource) {
        super(messageSource.getMessage(messageKey, null, null));
    }
}
