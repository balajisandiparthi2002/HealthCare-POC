package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dao.DoctorPatientAssignmentRepository;
import com.theelixrlabs.healthcare.dto.PatientDTO;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.model.DoctorPatientAssignmentModel;
import com.theelixrlabs.healthcare.model.PatientModel;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import com.theelixrlabs.healthcare.validation.Validator;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing patient-related operations.
 */
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    private final MessageSource messageSource;

    private final DoctorPatientAssignmentRepository doctorPatientAssignmentRepository;

    private final Validator validator;

    //Constructor injection
    public PatientService(PatientRepository patientRepository, MessageSource messageSource, DoctorPatientAssignmentRepository doctorPatientAssignmentRepository, Validator validator) {
        this.patientRepository = patientRepository;
        this.messageSource = messageSource;
        this.doctorPatientAssignmentRepository = doctorPatientAssignmentRepository;
        this.validator = validator;
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
        validator.validatePatientDTO(patientDTO);

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
     * @param validPatientId    Patient ID as UUID
     * @return PatientDTO object for the ID
     * @throws CustomException    If no patient found with the ID
     */
    public PatientDTO getPatientById(UUID validPatientId) throws CustomException {
        Optional<PatientModel> patientModelOptional = patientRepository.findById(validPatientId);
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
     * @param validPatientId    The ID of the patient to delete.
     * @return A success message upon successful deletion.
     * @throws CustomException    If the patient is not found or is currently assigned to a doctor.
     */
    public String deletePatientById(UUID validPatientId) throws CustomException {
        getPatientById(validPatientId);
        if (isPatientAssignedToDoctor(validPatientId)) {
            throw new CustomException(PatientConstants.PATIENT_DELETION_FAILED_ASSIGNED_TO_DOCTOR, messageSource);
        }
        patientRepository.deleteById(validPatientId);
        return messageSource.getMessage(PatientConstants.PATIENT_DELETE_SUCCESS_MESSAGE, new Object[]{validPatientId}, Locale.getDefault());
    }
}
