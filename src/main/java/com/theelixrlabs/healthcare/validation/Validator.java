package com.theelixrlabs.healthcare.validation;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dto.PatientDTO;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Validator {

    private final MessageSource messageSource;

    public Validator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Validates and converts a given string representation of UUID into a UUID object.
     * Throws a CustomException with a specific error message if the string is not a valid UUID format.
     *
     * @param id    The string representation of UUID to validate and convert.
     * @param errorMessage    The error message to use in the CustomException if validation fails.
     * @return The UUID object parsed from the input string.
     * @throws CustomException    If the input string is not a valid UUID format.
     */
    public UUID validateUUID(String id, String errorMessage) throws CustomException {
        UUID patientId;
        try {
            patientId = UUID.fromString(id);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new CustomException(errorMessage, messageSource);
        }
        return patientId;
    }

    /**
     * Validates the PatientDTO before adding details.
     *
     * @param patientDTO    The data transfer object containing patient information.
     * @throws CustomException    If validation fails (e.g., empty first name or invalid characters).
     */
    public void validatePatientDTO(PatientDTO patientDTO) throws CustomException {

        //Validate first name
        if (patientDTO.getPatientFirstName().isEmpty()) {
            throw new CustomException(PatientConstants.FIRST_NAME_NOT_EMPTY_KEY, messageSource);
        } else if (!patientDTO.getPatientFirstName().matches(PatientConstants.ALPHA_CHARACTERS_REGEX)) {
            throw new CustomException(PatientConstants.INVALID_FIRST_NAME_KEY, messageSource);
        }

        //Validate last name
        if (patientDTO.getPatientLastName().isEmpty()) {
            throw new CustomException(PatientConstants.LAST_NAME_SHOULD_NOT_BE_EMPTY_KEY, messageSource);
        } else if (!patientDTO.getPatientLastName().matches(PatientConstants.ALPHA_CHARACTERS_REGEX)) {
            throw new CustomException(PatientConstants.INVALID_LAST_NAME_KEY, messageSource);
        }
    }
}
