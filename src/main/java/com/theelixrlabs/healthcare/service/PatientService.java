package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dao.PatientRepository;
import com.theelixrlabs.healthcare.dto.PatientDTO;
import com.theelixrlabs.healthcare.exceptionhandler.CustomException;
import com.theelixrlabs.healthcare.model.PatientModel;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * Service class for managing patient-related operations.
 * Validations on the input parameters are done in this class.
 */
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    //Constructor injection of PatientRepository
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Validates the PatientDTO before adding details.
     *
     * @param patientDTO    The data transfer object containing patient information.
     * @throws CustomException    If validation fails (e.g., empty first name or invalid characters).
     */
    private void validatePatientDTO(PatientDTO patientDTO) throws CustomException {

        //Validate first name
        if (patientDTO.getFirstName().isEmpty()) {
            throw new CustomException(PatientConstants.FIRST_NAME_SHOULD_NOT_BE_EMPTY);
        } else if (!patientDTO.getFirstName().matches(PatientConstants.ALPHA_CHARACTERS_ONLY_REGEX)) {
            throw new CustomException(PatientConstants.INVALID_FIRST_NAME);
        }

        //Validate last name
        if (patientDTO.getLastName().isEmpty()) {
            throw new CustomException(PatientConstants.LAST_NAME_SHOULD_NOT_BE_EMPTY);
        } else if (!patientDTO.getLastName().matches(PatientConstants.ALPHA_CHARACTERS_ONLY_REGEX)) {
            throw new CustomException(PatientConstants.INVALID_LAST_NAME);
        }
    }

    /**
     * Creates a new patient based on the provided PatientDTO.
     *
     * @param patientDTO    The data transfer object containing patient information.
     * @return The PatientDTO of the newly created patient, with ID populated.
     * @throws CustomException    If validation fails or if the Aadhaar number already exists.
     */
    public PatientDTO addPatientDetails(PatientDTO patientDTO) throws CustomException {

        //Validate the incoming patientDTO
        validatePatientDTO(patientDTO);

        //Format Aadhaar number
        String aadhaarNumber = patientDTO.getAadhaarNumber();
        String formattedAadhaarNumber = String.format(PatientConstants.AADHAAR_FORMAT_PATTERN, aadhaarNumber.substring(0, 4), aadhaarNumber.substring(4, 8), aadhaarNumber.substring(8, 12));

        //Check if aadhaar number already exists.
        if (patientRepository.findByAadhaarNumber(formattedAadhaarNumber).isPresent()) {
            throw new CustomException(PatientConstants.AADHAAR_NUMBER_ALREADY_EXISTS);
        }

        //Generate UUID for new Patient
        UUID uuid = UUID.randomUUID();

        //create PatientModel instance to save in the database
        PatientModel patientModel = PatientModel.builder()
                .id(uuid)
                .firstName(patientDTO.getFirstName())
                .lastName(patientDTO.getLastName())
                .aadhaarNumber(formattedAadhaarNumber)
                .build();

        //save PatientModel to the database
        patientRepository.save(patientModel);

        //Populate the generated ID back to PatientDTO and return it.
        return PatientDTO.builder()
                .id(patientModel.getId())
                .firstName(patientModel.getFirstName())
                .lastName(patientModel.getLastName())
                .aadhaarNumber(patientModel.getAadhaarNumber())
                .build();
    }
}
