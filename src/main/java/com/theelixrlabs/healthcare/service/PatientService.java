package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dao.DoctorPatientAssignmentRepository;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.exceptionHandler.ResourceNotFoundException;
import com.theelixrlabs.healthcare.model.DoctorPatientAssignmentModel;
import com.theelixrlabs.healthcare.model.PatientModel;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import com.theelixrlabs.healthcare.validation.Validator;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing patient-related operations.
 */
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    private final MessageUtil messageUtil;

    private final DoctorPatientAssignmentRepository doctorPatientAssignmentRepository;

    private final Validator validator;

    //Constructor injection
    public PatientService(PatientRepository patientRepository, DoctorPatientAssignmentRepository doctorPatientAssignmentRepository, Validator validator, MessageUtil messageUtil) {
        this.patientRepository = patientRepository;
        this.doctorPatientAssignmentRepository = doctorPatientAssignmentRepository;
        this.validator = validator;
        this.messageUtil = messageUtil;
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
     * @param patientDto    The data transfer object containing patient information.
     * @return The PatientDTO of the newly created patient, with ID populated.
     * @throws CustomException    If validation fails or if the Aadhaar number already exists.
     */
    public PatientDto addPatientDetails(PatientDto patientDto) throws CustomException {

        //Validate the incoming patientDto
        validator.validatePatientDto(patientDto);

        //Format Aadhaar number
        String aadhaarNumber = patientDto.getPatientAadhaarNumber();
        String formattedAadhaarNumber = String.format(PatientConstants.AADHAAR_FORMAT_PATTERN, aadhaarNumber.substring(0, 4), aadhaarNumber.substring(4, 8), aadhaarNumber.substring(8, 12));

        //Check if aadhaar number already exists.
        if (patientRepository.findByPatientAadhaarNumber(formattedAadhaarNumber).isPresent()) {
            throw new CustomException(messageUtil.getMessage(MessageConstants.PATIENT_AADHAAR_NUMBER_EXISTS));
        }
        //Generate UUID for new Patient
        UUID uuid = UUID.randomUUID();

        //create PatientModel instance to save in the database
        PatientModel patientModel = PatientModel.builder()
                .id(uuid)
                .patientFirstName(patientDto.getPatientFirstName())
                .patientLastName(patientDto.getPatientLastName())
                .patientAadhaarNumber(formattedAadhaarNumber)
                .build();

        //save PatientModel to the database
        patientRepository.save(patientModel);

        //Populate the generated ID back to PatientDTO and return it.
        return PatientDto.builder()
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
    public PatientDto getPatientById(String patientId) throws CustomException {
        UUID validPatientId = validator.validateAndConvertToUUID(patientId, PatientConstants.INVALID_UUID_KEY);
        Optional<PatientModel> patientModelOptional = patientRepository.findById(validPatientId);
        if (patientModelOptional.isEmpty())
            throw new CustomException(messageUtil.getMessage(PatientConstants.PATIENT_NOT_FOUND_KEY));
        PatientModel patientModel = patientModelOptional.get();
        //mapping patient model to patientDTO
        return PatientDto.builder()
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
        UUID validPatientId = validator.validateAndConvertToUUID(patientId, PatientConstants.INVALID_UUID_KEY);
        if (!patientRepository.existsById(validPatientId))
            throw new ResourceNotFoundException(messageUtil.getMessage(PatientConstants.PATIENT_NOT_FOUND_KEY));
        if (isPatientAssignedToDoctor(validPatientId)) {
            throw new CustomException(messageUtil.getMessage(PatientConstants.PATIENT_DELETION_FAILED_ASSIGNED_TO_DOCTOR));
        }
        patientRepository.deleteById(validPatientId);
        return messageUtil.getMessage(PatientConstants.PATIENT_DELETE_SUCCESS_MESSAGE, new Object[]{validPatientId});
    }
}
