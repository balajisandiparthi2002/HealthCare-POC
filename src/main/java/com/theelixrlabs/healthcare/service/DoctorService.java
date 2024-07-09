package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.dao.DoctorPatientAssignmentRepository;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.model.DoctorModel;
import com.theelixrlabs.healthcare.model.DoctorPatientAssignmentModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class responsible for handling operations related to doctor.
 */
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    private final DoctorPatientAssignmentRepository doctorPatientAssignmentRepository;

    private final MessageUtil messageUtil;

    private final Validator validator;

    //Constructor Injection.
    public DoctorService(DoctorRepository doctorRepository, DoctorPatientAssignmentRepository doctorPatientAssignmentRepository, MessageUtil messageUtil, Validator validator) {
        this.doctorRepository = doctorRepository;
        this.doctorPatientAssignmentRepository = doctorPatientAssignmentRepository;
        this.messageUtil = messageUtil;
        this.validator = validator;
    }

    /**
     * Checks if a doctor is assigned to any patients.
     *
     * @param validDoctorId    The unique identifier of the doctor to check.
     * @return true if the doctor is assigned to at least one patient, false otherwise.
     */
    private boolean isDoctorAssignedToPatient(UUID validDoctorId) {
        List<DoctorPatientAssignmentModel> doctorAssignmentList = doctorPatientAssignmentRepository.findByDoctorId(validDoctorId);
        return !doctorAssignmentList.isEmpty();
    }

    /**
     * Saves a new doctor document based on the provided DoctorDto.
     *
     * @param doctorDto    DoctorDto object containing doctor information.
     * @return The saved dto object.
     */
    public DoctorDto saveDoctor(DoctorDto doctorDto) throws CustomException {
        validator.validateDoctor(doctorDto);
        String aadhaarNumber = doctorDto.getAadhaarNumber();

        String formattedAadhaarNumber = aadhaarNumber.substring(0, 4) + DoctorConstants.EMPTY_SPACE +
                aadhaarNumber.substring(4, 8) + DoctorConstants.EMPTY_SPACE +
                aadhaarNumber.substring(8, 12);
        if (doctorRepository.findByAadhaarNumber(formattedAadhaarNumber).isPresent()) {
            throw new CustomException(messageUtil.getMessage(MessageConstants.DOCTOR_AADHAAR_ALREADY_PRESENT));
        }
        UUID uuid = UUID.randomUUID();
        DoctorModel doctorModel = DoctorModel.builder()
                .id(uuid)
                .firstName(doctorDto.getFirstName())
                .lastName(doctorDto.getLastName())
                .department(doctorDto.getDepartment())
                .aadhaarNumber(formattedAadhaarNumber)
                .build();
        doctorRepository.save(doctorModel);
        DoctorDto savedDoctorDto = DoctorDto.builder()
                .id(doctorModel.getId())
                .firstName(doctorModel.getFirstName())
                .lastName(doctorModel.getLastName())
                .department(doctorModel.getDepartment())
                .aadhaarNumber(doctorModel.getAadhaarNumber())
                .build();
        return savedDoctorDto;
    }

    /**
     * @param doctorId    UUID of doctor in String format.
     * @return DoctorDto object containing doctor information.
     * @throws CustomException    Class to handle custom exception
     */
    public DoctorDto getDoctorById(String doctorId) throws CustomException {
        UUID validDoctorId=validator.validateAndConvertToUUID(doctorId,MessageConstants.DOCTOR_NOT_FOUND);
        Optional<DoctorModel> doctorModelOptional = doctorRepository.findById(validDoctorId);
        if (doctorModelOptional.isEmpty()) {
            throw new CustomException(messageUtil.getMessage(MessageConstants.DOCTOR_NOT_FOUND));
        }
        DoctorModel doctorModel = doctorModelOptional.get();
        return DoctorDto.builder()
                .id(doctorModel.getId())
                .firstName(doctorModel.getFirstName())
                .lastName(doctorModel.getLastName())
                .department(doctorModel.getDepartment())
                .aadhaarNumber(doctorModel.getAadhaarNumber())
                .build();
    }

    /**
     * Deletes a doctor by their id.
     *
     * @param doctorId    The string representation of the doctor's UUID.
     * @return A success message upon successful deletion.
     * @throws CustomException    If the doctor is not found or is assigned to any patients.
     */
    public String deleteDoctorById(String doctorId) throws CustomException {
        UUID validDoctorId = validator.validateAndConvertToUUID(doctorId, MessageConstants.INVALID_UUID);

        // Check if a doctor with the validDoctorId exists in the repository.
        if (!doctorRepository.existsById(validDoctorId))
            throw new CustomException(messageUtil.getMessage(MessageConstants.DOCTOR_NOT_FOUND));

        // Check if the doctor is assigned to any patients before deletion.
        if (isDoctorAssignedToPatient(validDoctorId)) {
            throw new CustomException(messageUtil.getMessage(MessageConstants.DOCTOR_DELETION_FAILED_ASSIGNED_TO_PATIENT));
        }

        // If no patients are assigned to the doctor, proceed with deletion.
        doctorRepository.deleteById(validDoctorId);
        return messageUtil.getMessage(MessageConstants.DOCTOR_DELETE_SUCCESS_MESSAGE, new Object[]{validDoctorId});
    }
}
