package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.DoctorPatientAssignmentConstants;
import com.theelixrlabs.healthcare.dao.DoctorPatientAssignmentRepository;
import com.theelixrlabs.healthcare.dto.DoctorPatientAssignmentDto;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.model.DoctorPatientAssignmentModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
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

    public DoctorPatientAssignmentService(DoctorPatientAssignmentRepository doctorPatientAssignmentRepository,
                                          PatientRepository patientRepository, DoctorRepository doctorRepository, MessageUtil messageUtil) {
        this.doctorPatientAssignmentRepository = doctorPatientAssignmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.messageUtil = messageUtil;
    }

    /**
     * Checks if doctor is previously assigned to same patient and if not then saves the doctorPatientAssignmentDto
     *
     * @param doctorPatientAssignmentDto DTO object containing doctorId and patientId
     * @return DoctorPatientAssignmentDto
     * @throws CustomException if any exception occurs
     */
    public DoctorPatientAssignmentDto assignDoctorToPatient(DoctorPatientAssignmentDto doctorPatientAssignmentDto) throws CustomException {
        validateDoctorPatientAssignment(doctorPatientAssignmentDto);
        UUID doctorId = UUID.fromString(doctorPatientAssignmentDto.getDoctorId());
        UUID patientId = UUID.fromString(doctorPatientAssignmentDto.getPatientId());
        if (isDoctorAlreadyAssigned(doctorId, patientId)) {
            throw new CustomException(messageUtil.getMessage(DoctorPatientAssignmentConstants.DOCTOR_ALREADY_ASSIGNED_KEY));
        }
        return mapDoctorPatientAssignment(doctorPatientAssignmentDto);
    }

    private void validateDoctorPatientAssignment(DoctorPatientAssignmentDto doctorPatientAssignmentDTO) throws CustomException {
        UUID doctorId, patientId;
        try {
            doctorId = UUID.fromString(doctorPatientAssignmentDTO.getDoctorId());
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new CustomException(messageUtil.getMessage(DoctorPatientAssignmentConstants.DOCTOR_ID_INVALID_KEY));
        }
        try {
            patientId = UUID.fromString(doctorPatientAssignmentDTO.getPatientId());
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new CustomException(messageUtil.getMessage(DoctorPatientAssignmentConstants.PATIENT_ID_INVALID_KEY));
        }
        if (!doctorRepository.existsById(doctorId)) {
            throw new CustomException(messageUtil.getMessage(DoctorPatientAssignmentConstants.DOCTOR_NOT_FOUND_KEY));
        }
        if (!patientRepository.existsById(patientId)) {
            throw new CustomException(messageUtil.getMessage(DoctorPatientAssignmentConstants.PATIENT_NOT_FOUND_KEY));
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
