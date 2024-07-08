package com.theelixrlabs.healthcare.validation;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;

public class Validator {
    /**
     * Validating method to validate based on incoming request.
     *
     * @param doctorDto Data transfer object containing doctor information.
     * @throws CustomException If validation fails or doctor already exists .
     */
    public void validateDoctor(DoctorDto doctorDto) throws CustomException {
        if (doctorDto.getFirstName() != null) {
            if (doctorDto.getFirstName().isEmpty()) {
                throw new CustomException(MessageConstants.FIRST_NAME_SHOULD_NOT_EMPTY);
            } else if (!doctorDto.getFirstName().matches(DoctorConstants.CHARACTER_ONLY_REGEX_PATTERN)) {
                throw new CustomException(MessageConstants.INVALID_FIRSTNAME);
            }
        }
        if (doctorDto.getLastName() != null) {
            if (doctorDto.getLastName().isEmpty()) {
                throw new CustomException(MessageConstants.LAST_NAME_SHOULD_NOT_EMPTY);
            } else if (!doctorDto.getLastName().matches(DoctorConstants.CHARACTER_ONLY_REGEX_PATTERN)) {
                throw new CustomException(MessageConstants.INVALID_LASTNAME);
            }
        }
        if (doctorDto.getDepartment() != null) {
            if (doctorDto.getDepartment().isEmpty()) {
                throw new CustomException(MessageConstants.DEPARTMENT_SHOULD_NOT_EMPTY);
            }
        }
        if (doctorDto.getAadhaarNumber() != null) {
            if (doctorDto.getAadhaarNumber().isEmpty()) {
                throw new CustomException(MessageConstants.AADHAAR_SHOULD_NOT_EMPTY);
            } else if (!(doctorDto.getAadhaarNumber().matches(DoctorConstants.AADHAAR_REGEX_PATTERN))) {
                throw new CustomException(MessageConstants.INVALID_AADHAAR_NUMBER);
            }
        }
    }
}
