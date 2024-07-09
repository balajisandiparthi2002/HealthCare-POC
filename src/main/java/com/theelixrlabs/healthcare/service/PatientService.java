package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dto.PatientDTO;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.model.PatientModel;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing patient-related operations.
 * Validations on the input parameters are done in this class.
 */
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final MessageUtil messageUtil;

    //Constructor injection of PatientRepository and MessageSource
    public PatientService(PatientRepository patientRepository, MessageUtil messageUtil) {
        this.patientRepository = patientRepository;
        this.messageUtil = messageUtil;
    }

    /**
     * Validates the PatientDTO before adding details.
     *
     * @param patientDTO The data transfer object containing patient information.
     * @throws CustomException If validation fails (e.g., empty first name or invalid characters).
     */
    private void validatePatientDTO(PatientDTO patientDTO) throws CustomException {

        //Validate first name
        if (patientDTO.getPatientFirstName().isEmpty()) {
            throw new CustomException(messageUtil.getMessage(PatientConstants.FIRST_NAME_NOT_EMPTY_KEY));
        } else if (!patientDTO.getPatientFirstName().matches(PatientConstants.ALPHA_CHARACTERS_ONLY_REGEX)) {
            throw new CustomException(messageUtil.getMessage(PatientConstants.INVALID_FIRST_NAME_KEY));
        }

        //Validate last name
        if (patientDTO.getPatientLastName().isEmpty()) {
            throw new CustomException(messageUtil.getMessage(PatientConstants.LAST_NAME_SHOULD_NOT_EMPTY_KEY));
        } else if (!patientDTO.getPatientLastName().matches(PatientConstants.ALPHA_CHARACTERS_ONLY_REGEX)) {
            throw new CustomException(messageUtil.getMessage(PatientConstants.INVALID_LAST_NAME_KEY));
        }
    }

    /**
     * Creates a new patient based on the provided PatientDTO.
     *
     * @param patientDTO The data transfer object containing patient information.
     * @return The PatientDTO of the newly created patient, with ID populated.
     * @throws CustomException If validation fails or if the Aadhaar number already exists.
     */
    public PatientDTO addPatientDetails(PatientDTO patientDTO) throws CustomException {

        //Validate the incoming patientDTO
        validatePatientDTO(patientDTO);

        //Format Aadhaar number
        String aadhaarNumber = patientDTO.getPatientAadhaarNumber();
        String formattedAadhaarNumber = String.format(PatientConstants.AADHAAR_FORMAT_PATTERN, aadhaarNumber.substring(0, 4), aadhaarNumber.substring(4, 8), aadhaarNumber.substring(8, 12));

        //Check if aadhaar number already exists.
        if (patientRepository.findByPatientAadhaarNumber(formattedAadhaarNumber).isPresent()) {
            throw new CustomException(messageUtil.getMessage(PatientConstants.AADHAAR_NUMBER_EXISTS_KEY));
        }

        //Generate UUID for new Patient
        UUID uuid = UUID.randomUUID();

        //create PatientModel instance to save in the database
        PatientModel patientModel = PatientModel.builder()
                .id(uuid)
                .patientFirstName(patientDTO.getPatientFirstName())
                .patientLastName(patientDTO.getPatientLastName())
                .patientAadhaarNumber(formattedAadhaarNumber)
                .build();

        //save PatientModel to the database
        patientRepository.save(patientModel);

        //Populate the generated ID back to PatientDTO and return it.
        return PatientDTO.builder()
                .id(patientModel.getId())
                .patientFirstName(patientModel.getPatientFirstName())
                .patientLastName(patientModel.getPatientLastName())
                .patientAadhaarNumber(patientModel.getPatientAadhaarNumber())
                .build();
    }

    /**
     * Get the PatientModel object associated with ID
     *
     * @param patientId Patient ID as UUID
     * @return PatientDTO object for the ID
     * @throws CustomException If no patient found with the ID
     */
    public PatientDTO getPatientById(UUID patientId) throws CustomException {
        Optional<PatientModel> patientModelOptional = patientRepository.findById(patientId);
        if (patientModelOptional.isEmpty()) throw new CustomException(messageUtil.getMessage(PatientConstants.PATIENT_NOT_FOUND_KEY));
        PatientModel patientModel = patientModelOptional.get();
        //mapping patient model to patientDTO
        return PatientDTO.builder()
                .id(patientModel.getId())
                .patientFirstName(patientModel.getPatientFirstName())
                .patientLastName(patientModel.getPatientLastName())
                .patientAadhaarNumber(patientModel.getPatientAadhaarNumber())
                .build();
    }
}
