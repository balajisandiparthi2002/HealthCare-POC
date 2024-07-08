package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dao.DoctorPatientAssignmentRepository;
import com.theelixrlabs.healthcare.dto.PatientDTO;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.model.DoctorPatientAssignmentModel;
import com.theelixrlabs.healthcare.model.PatientModel;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing patient-related operations.
 * Validations on the input parameters are done in this class.
 */
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    private final MessageSource messageSource;

    private final DoctorPatientAssignmentRepository doctorPatientAssignmentRepository;

    //Constructor injection of PatientRepository and MessageSource
    public PatientService(PatientRepository patientRepository, MessageSource messageSource, DoctorPatientAssignmentRepository doctorPatientAssignmentRepository) {
        this.patientRepository = patientRepository;
        this.messageSource = messageSource;
        this.doctorPatientAssignmentRepository = doctorPatientAssignmentRepository;
    }

    /**
     * Validates the PatientDTO before adding details.
     *
     * @param patientDTO    The data transfer object containing patient information.
     * @throws CustomException    If validation fails (e.g., empty first name or invalid characters).
     */
    private void validatePatientDTO(PatientDTO patientDTO) throws CustomException {

        //Validate first name
        if (patientDTO.getPatientFirstName().isEmpty()) {
            throw new CustomException(PatientConstants.FIRST_NAME_NOT_EMPTY_KEY, messageSource);
        } else if (!patientDTO.getPatientFirstName().matches(PatientConstants.ALPHA_CHARACTERS_ONLY_REGEX)) {
            throw new CustomException(PatientConstants.INVALID_FIRST_NAME_KEY, messageSource);
        }

        //Validate last name
        if (patientDTO.getPatientLastName().isEmpty()) {
            throw new CustomException(PatientConstants.LAST_NAME_SHOULD_NOT_EMPTY_KEY, messageSource);
        } else if (!patientDTO.getPatientLastName().matches(PatientConstants.ALPHA_CHARACTERS_ONLY_REGEX)) {
            throw new CustomException(PatientConstants.INVALID_LAST_NAME_KEY, messageSource);
        }
    }

    /**
     * Validates and converts a given string representation of UUID into a UUID object.
     * Throws a CustomException if the string is not a valid UUID format.
     *
     * @param id    The string representation of UUID to validate and convert.
     * @return The UUID object parsed from the input string.
     * @throws CustomException    If the input string is not a valid UUID format.
     */
    private UUID validateUUID(String id) throws CustomException {
        UUID patientId;
        try {
            patientId = UUID.fromString(id);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new CustomException(PatientConstants.INVALID_UUID_KEY, messageSource);
        }
        return patientId;
    }

    /**
     * Checks if a patient is assigned to any doctor based on the given patient ID.
     *
     * @param validPatientId    The UUID of the patient to check assignment for.
     * @return true if the patient is assigned to at least one doctor, false otherwise.
     */
    private boolean isPatientAssignedToDoctor(UUID validPatientId) {
        List<DoctorPatientAssignmentModel> patientAssignmentList = doctorPatientAssignmentRepository.findByPatientId(validPatientId);
        return !patientAssignmentList.isEmpty();
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
        String aadhaarNumber = patientDTO.getPatientAadhaarNumber();
        String formattedAadhaarNumber = String.format(PatientConstants.AADHAAR_FORMAT_PATTERN, aadhaarNumber.substring(0, 4), aadhaarNumber.substring(4, 8), aadhaarNumber.substring(8, 12));

        //Check if aadhaar number already exists.
        if (patientRepository.findByPatientAadhaarNumber(formattedAadhaarNumber).isPresent()) {
            throw new CustomException(PatientConstants.AADHAAR_NUMBER_EXISTS_KEY, messageSource);
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
     * @param patientId    Patient ID as UUID
     * @return PatientDTO object for the ID
     * @throws CustomException    If no patient found with the ID
     */
    public PatientDTO getPatientById(String patientId) throws CustomException {
        UUID patientID = validateUUID(patientId);
        Optional<PatientModel> patientModelOptional = patientRepository.findById(patientID);
        if (patientModelOptional.isEmpty())
            throw new CustomException(PatientConstants.PATIENT_NOT_FOUND_KEY, messageSource);
        PatientModel patientModel = patientModelOptional.get();
        //mapping patient model to patientDTO
        return PatientDTO.builder()
                .id(patientModel.getId())
                .patientFirstName(patientModel.getPatientFirstName())
                .patientLastName(patientModel.getPatientLastName())
                .patientAadhaarNumber(patientModel.getPatientAadhaarNumber())
                .build();
    }

    /**
     * Deletes a patient by their ID, if conditions are met.
     *
     * @param patientId    The ID of the patient to delete.
     * @return A success message upon successful deletion.
     * @throws CustomException    If the patient is not found or is currently assigned to a doctor.
     */
    public String deletePatientById(String patientId) throws CustomException {
        UUID validPatientId = validateUUID(patientId);
        getPatientById(patientId);
        if (isPatientAssignedToDoctor(validPatientId)) {
            throw new CustomException(PatientConstants.PATIENT_DELETION_FAILED_ASSIGNED_TO_DOCTOR, messageSource);
        }
        patientRepository.deleteById(validPatientId);
        return messageSource.getMessage(PatientConstants.PATIENT_DELETE_SUCCESS_MESSAGE, new Object[]{patientId}, Locale.getDefault());
    }
}
