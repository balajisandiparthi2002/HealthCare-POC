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
     * Replace existing doctor details based on request.
     *
     * @param patientId  Doctor unique UUID
     * @param patientDTO Data transfer object containing doctor information.
     * @return The replaced doctor details.
     * @throws CustomException If validation fails or doctor already exists.
     */
    public PatientDto patchPatientById(UUID patientId, PatientDto patientDTO) throws CustomException {
        validator.validatePatchPatient(patientDTO);
        Optional<PatientModel> optionalDoctor = patientRepository.findById(patientId);
        if (optionalDoctor.isPresent()) {
            PatientModel existingPatient = optionalDoctor.get();
            if (patientDTO.getPatientFirstName() != null && !patientDTO.getPatientFirstName().equals(existingPatient.getPatientFirstName())) {
                existingPatient.setPatientFirstName(patientDTO.getPatientFirstName());
            }
            if (patientDTO.getPatientLastName() != null && !patientDTO.getPatientLastName().equals(existingPatient.getPatientLastName())) {
                existingPatient.setPatientFirstName(patientDTO.getPatientLastName());
            }
            if (patientDTO.getPatientAadhaarNumber() != null && !patientDTO.getPatientAadhaarNumber().equals(existingPatient.getPatientAadhaarNumber())) {
                String aadhaarNumber = patientDTO.getPatientAadhaarNumber();
                String formattedAadhaarNumber = aadhaarNumber.substring(0, 4) + PatientConstants.EMPTY_SPACE +
                        aadhaarNumber.substring(4, 8) + PatientConstants.EMPTY_SPACE +
                        aadhaarNumber.substring(8, 12);
                if (patientRepository.findByPatientAadhaarNumber(formattedAadhaarNumber).isPresent() || doctorRepository.findByAadhaarNumber(formattedAadhaarNumber).isPresent()) {
                    throw new CustomException(messageUtil.getMessage(MessageConstants.PATIENT_AADHAAR_NUMBER_EXISTS));
                }
                existingPatient.setPatientAadhaarNumber(formattedAadhaarNumber);
            }
            patientRepository.save(existingPatient);
            return PatientDto.builder()
                    .id(existingPatient.getId())
                    .patientFirstName(existingPatient.getPatientFirstName())
                    .patientLastName(existingPatient.getPatientLastName())
                    .patientAadhaarNumber(existingPatient.getPatientAadhaarNumber())
                    .build();
        } else {
            throw new ResourceNotFoundException(messageUtil.getMessage(PatientConstants.PATIENT_NOT_FOUND_KEY));
        }
    }
}
