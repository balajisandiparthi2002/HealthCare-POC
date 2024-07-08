package com.theelixrlabs.healthcare.validation;

import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class DoctorModelValidator {
    private final MessageSource messageSource;

    public DoctorModelValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void validateDoctorName(String doctorName) {
        if (StringUtils.isBlank(doctorName)) {
            throw new CustomException(MessageConstants.DOCTOR_NAME_CANNOT_BE_EMPTY, messageSource);
        }
    }
}
