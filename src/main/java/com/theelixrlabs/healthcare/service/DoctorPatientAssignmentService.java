package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.DoctorPatientAssignmentConstants;
import com.theelixrlabs.healthcare.exceptionHandler.ResourceNotFoundException;
import com.theelixrlabs.healthcare.repository.DoctorPatientAssignmentRepository;
import com.theelixrlabs.healthcare.dto.DoctorPatientAssignmentDto;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.model.DoctorPatientAssignmentModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class DoctorPatientAssignmentService {

    private final DoctorPatientAssignmentRepository doctorPatientAssignmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MessageUtil messageUtil;
    private final Validator validator;


    public DoctorPatientAssignmentService(DoctorPatientAssignmentRepository doctorPatientAssignmentRepository,
                                          PatientRepository patientRepository, DoctorRepository doctorRepository, MessageUtil messageUtil, Validator validator) {
        this.doctorPatientAssignmentRepository = doctorPatientAssignmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.messageUtil = messageUtil;
        this.validator = validator;
    }

    /**
     * Checks if doctor is previously assigned to same patient and if not then saves the doctorPatientAssignmentDto
     *
     * @param doctorPatientAssignmentDto DTO object containing doctorId and patientId
     * @return DoctorPatientAssignmentDto
     * @throws CustomException if any exception occurs
     */
    public DoctorPatientAssignmentDto assignDoctorToPatient(DoctorPatientAssignmentDto doctorPatientAssignmentDto) throws CustomException {
        UUID doctorId = validator.validateAndConvertToUUID(doctorPatientAssignmentDto.getDoctorId(),
                DoctorPatientAssignmentConstants.DOCTOR_ID_INVALID_KEY);
        UUID patientId = validator.validateAndConvertToUUID(doctorPatientAssignmentDto.getPatientId(),
                DoctorPatientAssignmentConstants.PATIENT_ID_INVALID_KEY);
        validateDoctorPatientExistence(doctorId, patientId);
        if (isDoctorAlreadyAssigned(doctorId, patientId)) {
            throw new CustomException(messageUtil.getMessage(DoctorPatientAssignmentConstants.DOCTOR_ALREADY_ASSIGNED_KEY));
        }
        return mapDoctorPatientAssignment(doctorPatientAssignmentDto);
    }

    /**
     * checks for doctor patient assignment and if present then unassigns doctor from patient
     *
     * @param doctorPatientAssignmentDto DTO object containing doctorId and patientId
     * @throws CustomException if any exception occurs
     */
    public void unassignDoctorFromPatient(DoctorPatientAssignmentDto doctorPatientAssignmentDto) throws CustomException {
        UUID doctorId = validator.validateAndConvertToUUID(doctorPatientAssignmentDto.getDoctorId(),
                DoctorPatientAssignmentConstants.DOCTOR_ID_INVALID_KEY);
        UUID patientId = validator.validateAndConvertToUUID(doctorPatientAssignmentDto.getPatientId(),
                DoctorPatientAssignmentConstants.PATIENT_ID_INVALID_KEY);
        validateDoctorPatientExistence(doctorId, patientId);
        Optional<DoctorPatientAssignmentModel> optionalDoctorPatientAssignmentModel =
                doctorPatientAssignmentRepository.findByDoctorIdAndPatientIdAndDateOfUnassignmentNull(doctorId, patientId);
        if (!doctorPatientAssignmentRepository.existsByDoctorIdAndPatientId(doctorId, patientId)) {
            throw new CustomException(messageUtil.getMessage(DoctorPatientAssignmentConstants.NO_ASSIGNMENT_EXISTS_KEY));
        }
        if (optionalDoctorPatientAssignmentModel.isEmpty()) {
            throw new CustomException(messageUtil.getMessage(DoctorPatientAssignmentConstants.DOCTOR_ALREADY_UNASSIGNED_KEY));
        }
        DoctorPatientAssignmentModel activeDoctorPatientAssignmentModel = optionalDoctorPatientAssignmentModel.get();
        activeDoctorPatientAssignmentModel.setDateOfUnassignment(Date.from(Instant.now()));
        doctorPatientAssignmentRepository.save(activeDoctorPatientAssignmentModel);
    }

    private void validateDoctorPatientExistence(UUID doctorId, UUID patientId) throws CustomException {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException(messageUtil.getMessage(DoctorPatientAssignmentConstants.DOCTOR_NOT_FOUND_KEY));
        }
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(messageUtil.getMessage(DoctorPatientAssignmentConstants.PATIENT_NOT_FOUND_KEY));
        }
    }

    private boolean isDoctorAlreadyAssigned(UUID doctorId, UUID patientId) {
        Optional<DoctorPatientAssignmentModel> activeDoctorPatientAssignmentModel = doctorPatientAssignmentRepository
                .findByDoctorIdAndPatientIdAndDateOfUnassignmentNull(doctorId, patientId);
        return activeDoctorPatientAssignmentModel.isPresent();
    }

    private DoctorPatientAssignmentDto mapDoctorPatientAssignment(DoctorPatientAssignmentDto doctorPatientAssignmentDTO) {
        DoctorPatientAssignmentDto responseDoctorPatientAssignmentDto;
        DoctorPatientAssignmentModel doctorPatientAssignmentModel = DoctorPatientAssignmentModel.builder()
                .id(UUID.randomUUID())
                .doctorId(UUID.fromString(doctorPatientAssignmentDTO.getDoctorId()))
                .patientId(UUID.fromString(doctorPatientAssignmentDTO.getPatientId()))
                .dateOfAssignment(Date.from(Instant.now()))
                .dateOfUnassignment(null)
                .build();
        doctorPatientAssignmentRepository.save(doctorPatientAssignmentModel);
        responseDoctorPatientAssignmentDto = DoctorPatientAssignmentDto.builder()
                .id(doctorPatientAssignmentModel.getId())
                .doctorId(doctorPatientAssignmentModel.getDoctorId().toString())
                .patientId(doctorPatientAssignmentModel.getPatientId().toString())
                .dateOfOperation(doctorPatientAssignmentModel.getDateOfAssignment())
                .build();
        return responseDoctorPatientAssignmentDto;
    }
}
