package com.theelixrlabs.healthcare.validation;

import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DoctorModelValidator {
    private final MessageUtil messageUtil;

    public DoctorModelValidator(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    public void validateDoctorName(String doctorName) {
        if (StringUtils.isBlank(doctorName)) {
            throw new CustomException(messageUtil.getMessage(MessageConstants.DOCTOR_NAME_CANNOT_BE_EMPTY));
        }
    }
}
