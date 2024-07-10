package com.theelixrlabs.healthcare.validation;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Validator {

    private final MessageUtil messageUtil;

    public Validator(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    /**
     * Validates and converts a given string representation of UUID into a UUID object.
     * Throws a CustomException with a specific error message if the string is not a valid UUID format.
     *
     * @param id           The string representation of UUID to validate and convert.
     * @param errorMessage The error message to use in the CustomException if validation fails.
     * @return The UUID object parsed from the input string.
     * @throws CustomException If the input string is not a valid UUID format.
     */
    public UUID validateAndConvertToUUID(String id, String errorMessage) throws CustomException {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new CustomException(messageUtil.getMessage(errorMessage));
        }
        return uuid;
    }

    /**
     * Validates the PatientDTO before adding details.
     *
     * @param patientDto The data transfer object containing patient information.
     * @throws CustomException If validation fails (e.g., empty first name or invalid characters).
     */
    public void validatePatientDto(PatientDto patientDto) throws CustomException {

        //Validate first name
        if (patientDto.getPatientFirstName().isEmpty()) {
            throw new CustomException(messageUtil.getMessage(PatientConstants.FIRST_NAME_NOT_EMPTY_KEY));
        } else if (!patientDto.getPatientFirstName().matches(PatientConstants.ALPHA_CHARACTERS_REGEX)) {
            throw new CustomException(messageUtil.getMessage(PatientConstants.INVALID_FIRST_NAME_KEY));
        }

        //Validate last name
        if (patientDto.getPatientLastName().isEmpty()) {
            throw new CustomException(messageUtil.getMessage(PatientConstants.LAST_NAME_SHOULD_NOT_BE_EMPTY_KEY));
        } else if (!patientDto.getPatientLastName().matches(PatientConstants.ALPHA_CHARACTERS_REGEX)) {
            throw new CustomException(messageUtil.getMessage(PatientConstants.INVALID_LAST_NAME_KEY));
        }
    }

    /**
     * Validating method to validate based on incoming request.
     *
     * @param doctorDto Data transfer object containing doctor information.
     * @throws CustomException If validation fails or doctor already exists .
     */
    public void validateDoctor(DoctorDto doctorDto) throws CustomException {
        if (doctorDto.getFirstName() != null) {
            if (doctorDto.getFirstName().isEmpty()) {
                throw new CustomException(messageUtil.getMessage(MessageConstants.DOCTOR_FIRST_NAME_SHOULD_NOT_BE_EMPTY));
            }
            if (!doctorDto.getFirstName().matches(DoctorConstants.CHARACTER_ONLY_REGEX_PATTERN)) {
                throw new CustomException(messageUtil.getMessage(MessageConstants.DOCTOR_INVALID_FIRSTNAME));
            }
        }
        if (doctorDto.getLastName() != null) {
            if (doctorDto.getLastName().isEmpty()) {
                throw new CustomException(messageUtil.getMessage(MessageConstants.DOCTOR_LAST_NAME_SHOULD_NOT_BE_EMPTY));
            }
            if (!doctorDto.getLastName().matches(DoctorConstants.CHARACTER_ONLY_REGEX_PATTERN)) {
                throw new CustomException(messageUtil.getMessage(MessageConstants.DOCTOR_INVALID_LASTNAME));
            }
        }
        if (doctorDto.getDepartment() != null) {
            if (doctorDto.getDepartment().isEmpty()) {
                throw new CustomException(messageUtil.getMessage(MessageConstants.DEPARTMENT_SHOULD_NOT_BE_EMPTY));
            }
        }
        if (doctorDto.getAadhaarNumber() != null) {
            if (doctorDto.getAadhaarNumber().isEmpty()) {
                throw new CustomException(messageUtil.getMessage(MessageConstants.DOCTOR_AADHAAR_NUMBER_SHOULD_NOT_BE_EMPTY));
            }
            if (!(doctorDto.getAadhaarNumber().matches(DoctorConstants.AADHAAR_REGEX_PATTERN))) {
                throw new CustomException(messageUtil.getMessage(MessageConstants.DOCTOR_INVALID_AADHAAR_NUMBER));
            }
        }
    }

    /**
     * Validating method to validate based on incoming request.
     *
     * @param patientDto Data transfer object containing doctor information.
     * @throws CustomException If validation fails or doctor already exists .
     */
    public void validatePatchPatient(PatientDto patientDto) throws CustomException {
        if (patientDto.getPatientFirstName() != null) {
            if (patientDto.getPatientFirstName().isEmpty()) {
                throw new CustomException(messageUtil.getMessage(PatientConstants.FIRST_NAME_NOT_EMPTY_KEY));
            }
            if (!patientDto.getPatientFirstName().matches(PatientConstants.ALPHA_CHARACTERS_REGEX)) {
                throw new CustomException(messageUtil.getMessage(PatientConstants.INVALID_FIRST_NAME_KEY));
            }
        }
        if (patientDto.getPatientLastName() != null) {
            if (patientDto.getPatientLastName().isEmpty()) {
                throw new CustomException(messageUtil.getMessage(PatientConstants.LAST_NAME_SHOULD_NOT_BE_EMPTY_KEY));
            }
            if (!patientDto.getPatientLastName().matches(PatientConstants.ALPHA_CHARACTERS_REGEX)) {
                throw new CustomException(messageUtil.getMessage(PatientConstants.INVALID_LAST_NAME_KEY));
            }
        }
        if (patientDto.getPatientAadhaarNumber() != null) {
            if (patientDto.getPatientAadhaarNumber().isEmpty()) {
                throw new CustomException(messageUtil.getMessage(MessageConstants.PATIENT_AADHAAR_NUMBER_SHOULD_NOT_BE_EMPTY));
            }
            if (!(patientDto.getPatientAadhaarNumber().matches(PatientConstants.AADHAAR_NUMBER_REGEX))) {
                throw new CustomException(messageUtil.getMessage(MessageConstants.PATIENT_INVALID_AADHAAR_NUMBER));
            }
        }
    }
}
