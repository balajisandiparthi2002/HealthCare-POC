package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.DoctorPatientAssignmentConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.dto.AssignedDoctorDto;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.dto.DoctorPatientAssignmentDto;
import com.theelixrlabs.healthcare.dto.DoctorsByPatientDto;
import com.theelixrlabs.healthcare.exceptionHandler.DoctorNotFoundException;
import com.theelixrlabs.healthcare.exceptionHandler.DoctorPatientAssignmentException;
import com.theelixrlabs.healthcare.exceptionHandler.PatientNotFoundException;
import com.theelixrlabs.healthcare.model.PatientModel;
import com.theelixrlabs.healthcare.repository.DoctorPatientAssignmentRepository;
import com.theelixrlabs.healthcare.model.DoctorPatientAssignmentModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;
import java.util.List;
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
     * @param doctorPatientAssignmentDto    DTO object containing doctorId and patientId
     * @return DoctorPatientAssignmentDto
     */
    public DoctorPatientAssignmentDto assignDoctorToPatient(DoctorPatientAssignmentDto doctorPatientAssignmentDto) throws Exception {
        UUID doctorId = validator.validateAndConvertToUUID(doctorPatientAssignmentDto.getDoctorId(),
                DoctorPatientAssignmentConstants.DOCTOR_ID_INVALID_KEY);
        UUID patientId = validator.validateAndConvertToUUID(doctorPatientAssignmentDto.getPatientId(),
                DoctorPatientAssignmentConstants.PATIENT_ID_INVALID_KEY);
        validateDoctorPatientExistence(doctorId, patientId);
        if (isDoctorAlreadyAssigned(doctorId, patientId)) {
            throw new DoctorPatientAssignmentException(messageUtil.getMessage(DoctorPatientAssignmentConstants.DOCTOR_ALREADY_ASSIGNED_KEY));
        }
        return mapDoctorPatientAssignment(doctorPatientAssignmentDto);
    }

    /**
     * checks for doctor patient assignment and if present then unassigns doctor from patient
     *
     * @param doctorPatientAssignmentDto    DTO object containing doctorId and patientId
     */
    public void unassignDoctorFromPatient(DoctorPatientAssignmentDto doctorPatientAssignmentDto) throws Exception {
        UUID doctorId = validator.validateAndConvertToUUID(doctorPatientAssignmentDto.getDoctorId(),
                DoctorPatientAssignmentConstants.DOCTOR_ID_INVALID_KEY);
        UUID patientId = validator.validateAndConvertToUUID(doctorPatientAssignmentDto.getPatientId(),
                DoctorPatientAssignmentConstants.PATIENT_ID_INVALID_KEY);
        validateDoctorPatientExistence(doctorId, patientId);
        Optional<DoctorPatientAssignmentModel> optionalDoctorPatientAssignmentModel =
                doctorPatientAssignmentRepository.findByDoctorIdAndPatientIdAndDateOfUnassignmentNull(doctorId, patientId);
        if (!doctorPatientAssignmentRepository.existsByDoctorIdAndPatientId(doctorId, patientId)) {
            throw new DoctorPatientAssignmentException(messageUtil.getMessage(DoctorPatientAssignmentConstants.NO_ASSIGNMENT_EXISTS_KEY));
        }
        if (optionalDoctorPatientAssignmentModel.isEmpty()) {
            throw new DoctorPatientAssignmentException(messageUtil.getMessage(DoctorPatientAssignmentConstants.DOCTOR_ALREADY_UNASSIGNED_KEY));
        }
        DoctorPatientAssignmentModel activeDoctorPatientAssignmentModel = optionalDoctorPatientAssignmentModel.get();
        activeDoctorPatientAssignmentModel.setDateOfUnassignment(Date.from(Instant.now()));
        doctorPatientAssignmentRepository.save(activeDoctorPatientAssignmentModel);
    }

    /**
     * Retrieves patient details and their assigned doctors based on patient ID.
     *
     * @param patientId    The string representation of patient ID.
     * @return DoctorsByPatientDto containing patient details and their assigned doctors.
     * @throws Exception    if patient is not found or not assigned to any doctors.
     */
    public DoctorsByPatientDto getDoctorsByPatientId(String patientId) throws Exception {
        UUID validPatientId = validator.validateAndConvertToUUID(patientId, MessageConstants.INVALID_UUID);
        Optional<PatientModel> optionalPatientModel = patientRepository.findById(validPatientId);
        if (optionalPatientModel.isEmpty()) {
            throw new PatientNotFoundException(messageUtil.getMessage(DoctorPatientAssignmentConstants.PATIENT_NOT_FOUND_KEY));
        }
        List<AssignedDoctorDto> assignedDoctorDtoList = doctorPatientAssignmentRepository.getDoctorsByPatientId(validPatientId);
        if (assignedDoctorDtoList.isEmpty()) {
            throw new DoctorPatientAssignmentException(messageUtil.getMessage(MessageConstants.PATIENT_NOT_ASSIGNED_TO_DOCTORS, new Object[]{validPatientId}));
        }
        List<DoctorDto> assignedDoctorsList = assignedDoctorDtoList.get(0).getAssignedDoctors();
        PatientModel patientModel = optionalPatientModel.get();
        PatientDto patientDto = PatientDto.builder()
                .id(patientModel.getId())
                .patientFirstName(patientModel.getPatientFirstName())
                .patientLastName(patientModel.getPatientLastName())
                .patientAadhaarNumber(patientModel.getPatientAadhaarNumber()).build();
        return new DoctorsByPatientDto(patientDto, assignedDoctorsList);
    }

    private void validateDoctorPatientExistence(UUID doctorId, UUID patientId) throws Exception {
        if (!doctorRepository.existsById(doctorId)) {
            throw new DoctorNotFoundException(messageUtil.getMessage(DoctorPatientAssignmentConstants.DOCTOR_NOT_FOUND_KEY));
        }
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException(messageUtil.getMessage(DoctorPatientAssignmentConstants.PATIENT_NOT_FOUND_KEY));
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
