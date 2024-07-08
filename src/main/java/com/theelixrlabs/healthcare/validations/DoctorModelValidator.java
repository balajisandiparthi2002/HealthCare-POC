package com.theelixrlabs.healthcare.validations;

import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class DoctorModelValidator {
    private final MessageSource messageSource;

    public DoctorModelValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void validateDoctorName(String doctorName) {
        if (doctorName == null || doctorName.trim().isEmpty()) {
            throw new CustomException(MessageConstants.DOCTOR_NAME_CANNOT_BE_EMPTY, messageSource);
        }
    }
}
