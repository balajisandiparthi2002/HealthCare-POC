package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.DoctorPatientAssignmentConstants;
import com.theelixrlabs.healthcare.dao.DoctorPatientAssignmentRepository;
import com.theelixrlabs.healthcare.dto.DoctorPatientAssignmentDTO;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.model.DoctorPatientAssignmentModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import org.springframework.context.MessageSource;
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
    private final MessageSource messageSource;

    public DoctorPatientAssignmentService(DoctorPatientAssignmentRepository doctorPatientAssignmentRepository,
                                          PatientRepository patientRepository, DoctorRepository doctorRepository,
                                          MessageSource messageSource) {
        this.doctorPatientAssignmentRepository = doctorPatientAssignmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.messageSource = messageSource;
    }


    private void validateDoctorPatientAssignmentDTO(DoctorPatientAssignmentDTO doctorPatientAssignmentDTO) throws CustomException {
        UUID doctorId, patientId;
        try {
            doctorId = UUID.fromString(doctorPatientAssignmentDTO.getDoctorID());
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new CustomException(DoctorPatientAssignmentConstants.DOCTOR_ID_INVALID_KEY, messageSource);
        }
        try {
            patientId = UUID.fromString(doctorPatientAssignmentDTO.getPatientID());
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new CustomException(DoctorPatientAssignmentConstants.PATIENT_ID_INVALID_KEY, messageSource);
        }
        if (!doctorRepository.existsById(doctorId))
            throw new CustomException(DoctorPatientAssignmentConstants.DOCTOR_NOT_FOUND_KEY, messageSource);
        if (!patientRepository.existsById(patientId))
            throw new CustomException(DoctorPatientAssignmentConstants.PATIENT_NOT_FOUND_KEY, messageSource);
    }

    /**
     * Checks if doctor is previously assigned to same patient and if not then saves the doctorPatientAssignmentDTO
     *
     * @param doctorPatientAssignmentDTO DTO object containing doctorId and patientId
     * @return DoctorPatientAssignmentDTO
     * @throws CustomException if any exception occurs
     */
    public DoctorPatientAssignmentDTO assignDoctorToPatient(DoctorPatientAssignmentDTO doctorPatientAssignmentDTO) throws CustomException {
        validateDoctorPatientAssignmentDTO(doctorPatientAssignmentDTO);
        List<DoctorPatientAssignmentModel> doctorPatientAssignmentList =
                doctorPatientAssignmentRepository.findByDoctorIDAndPatientID
                        (UUID.fromString(doctorPatientAssignmentDTO.getDoctorID()),
                                UUID.fromString(doctorPatientAssignmentDTO.getPatientID()));
        if (!doctorPatientAssignmentList.isEmpty()) {
            for (DoctorPatientAssignmentModel doctorPatientAssignmentModel : doctorPatientAssignmentList) {
                if (doctorPatientAssignmentModel.getDateOfUnassignment() == null) {
                    throw new CustomException(DoctorPatientAssignmentConstants.DOCTOR_ALREADY_ASSIGNED_KEY, messageSource);
                }
            }
        }
        return mapDoctorPatientAssignmentDTOToModel(doctorPatientAssignmentDTO);
    }

    private DoctorPatientAssignmentDTO mapDoctorPatientAssignmentDTOToModel(DoctorPatientAssignmentDTO doctorPatientAssignmentDTO) {
        DoctorPatientAssignmentDTO responseDoctorPatientAssignmentDTO;
        DoctorPatientAssignmentModel doctorPatientAssignmentModel = DoctorPatientAssignmentModel.builder()
                .id(UUID.randomUUID())
                .doctorID(UUID.fromString(doctorPatientAssignmentDTO.getDoctorID()))
                .patientID(UUID.fromString(doctorPatientAssignmentDTO.getPatientID()))
                .dateOfAssignment(Date.from(Instant.now()))
                .dateOfUnassignment(null)
                .build();
        doctorPatientAssignmentRepository.save(doctorPatientAssignmentModel);
        responseDoctorPatientAssignmentDTO = DoctorPatientAssignmentDTO.builder()
                .id(doctorPatientAssignmentModel.getId())
                .doctorID(doctorPatientAssignmentModel.getDoctorID().toString())
                .patientID(doctorPatientAssignmentModel.getPatientID().toString())
                .dateOfOperation(doctorPatientAssignmentModel.getDateOfAssignment())
                .build();
        return responseDoctorPatientAssignmentDTO;
    }
}
