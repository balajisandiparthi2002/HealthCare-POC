package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.exceptionHandler.ResourceNotFoundException;
import com.theelixrlabs.healthcare.model.PatientModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PatchPatientService {
    private final PatientRepository patientRepository;
    private final Validator validator;
    private final MessageUtil messageUtil;
    private final DoctorRepository doctorRepository;

    public PatchPatientService(PatientRepository patientRepository, Validator validator, MessageUtil messageUtil, DoctorRepository doctorRepository) {
        this.patientRepository = patientRepository;
        this.validator = validator;
        this.messageUtil = messageUtil;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Replace existing patient details based on request.
     *
     * @param patientId  Patient unique UUID
     * @param patientDto Data transfer object containing patient information.
     * @return The replaced patient details.
     * @throws CustomException If validation fails or patient already exists.
     */
    public PatientDto patchPatientById(String patientId, PatientDto patientDto) throws CustomException {
        UUID validPatientId = validator.validateAndConvertToUUID(patientId, MessageConstants.INVALID_UUID);
        validator.validatePatchPatient(patientDto);
        Optional<PatientModel> optionalPatient = patientRepository.findById(validPatientId);
        if (optionalPatient.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage(PatientConstants.PATIENT_NOT_FOUND_KEY));
        }
        PatientModel existingPatient = optionalPatient.get();
        if (!StringUtils.isBlank(patientDto.getPatientAadhaarNumber()) && !patientDto.getPatientAadhaarNumber().equals(existingPatient.getPatientAadhaarNumber())) {
            String aadhaarNumber = patientDto.getPatientAadhaarNumber();
            String formattedAadhaarNumber = formatAadhaarNumber(aadhaarNumber);
            if (patientRepository.findByPatientAadhaarNumber(formattedAadhaarNumber).isPresent() || doctorRepository.findByAadhaarNumber(formattedAadhaarNumber).isPresent()) {
                throw new CustomException(messageUtil.getMessage(MessageConstants.PATIENT_AADHAAR_NUMBER_EXISTS));
            }
            existingPatient.setPatientAadhaarNumber(formattedAadhaarNumber);
        }
        if (isFieldNullOrUnchanged(patientDto.getPatientFirstName(), existingPatient.getPatientFirstName())) {
            existingPatient.setPatientFirstName(patientDto.getPatientFirstName());
        }
        if (isFieldNullOrUnchanged(patientDto.getPatientLastName(), existingPatient.getPatientLastName())) {
            existingPatient.setPatientLastName(patientDto.getPatientLastName());
        }
        patientRepository.save(existingPatient);
        return PatientDto.builder()
                .id(existingPatient.getId())
                .patientFirstName(existingPatient.getPatientFirstName())
                .patientLastName(existingPatient.getPatientLastName())
                .patientAadhaarNumber(existingPatient.getPatientAadhaarNumber())
                .build();
    }

    private boolean isFieldNullOrUnchanged(String newValue, String existingValue) {
        return !StringUtils.isBlank(newValue) && !newValue.equals(existingValue);
    }

    private String formatAadhaarNumber(String aadhaarNumber) {
        return aadhaarNumber.substring(0, 4) + StringUtils.SPACE +
                aadhaarNumber.substring(4, 8) + StringUtils.SPACE +
                aadhaarNumber.substring(8, 12);
    }
}
