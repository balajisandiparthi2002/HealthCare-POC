package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.exceptionHandler.ResourceNotFoundException;
import com.theelixrlabs.healthcare.model.DoctorModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service class for facilitating partial updates to doctor details.
 */
@Service
public class PatchDoctorService {

    private final DoctorRepository doctorRepository;
    private final Validator validator;
    private final MessageUtil messageUtil;

    public PatchDoctorService(DoctorRepository doctorRepository, Validator validator, MessageUtil messageUtil) {
        this.doctorRepository = doctorRepository;
        this.validator = validator;
        this.messageUtil = messageUtil;
    }

    /**
     * Replace existing doctor details based on request.
     *
     * @param doctorId  Doctor unique UUID
     * @param doctorDto Data transfer object containing doctor information.
     * @return The replaced doctor details.
     * @throws CustomException If validation fails or doctor already exists.
     */
    public DoctorDto patchDoctorById(UUID doctorId, DoctorDto doctorDto) throws CustomException {
        validator.validateDoctor(doctorDto);
        Optional<DoctorModel> optionalDoctor = doctorRepository.findById(doctorId);
        if (optionalDoctor.isPresent()) {
            DoctorModel existingDoctor = optionalDoctor.get();
            if (doctorDto.getFirstName() != null && !doctorDto.getFirstName().equals(existingDoctor.getFirstName())) {
                existingDoctor.setFirstName(doctorDto.getFirstName());
            }
            if (doctorDto.getLastName() != null && !doctorDto.getLastName().equals(existingDoctor.getLastName())) {
                existingDoctor.setLastName(doctorDto.getLastName());
            }
            if (doctorDto.getDepartment() != null && !doctorDto.getDepartment().equals(existingDoctor.getDepartment())) {
                existingDoctor.setDepartment(doctorDto.getDepartment());
            }
            if (doctorDto.getAadhaarNumber() != null && !doctorDto.getAadhaarNumber().equals(existingDoctor.getAadhaarNumber())) {
                String aadhaarNumber = doctorDto.getAadhaarNumber();
                String formattedAadhaarNumber = aadhaarNumber.substring(0, 4) + DoctorConstants.EMPTY_SPACE +
                        aadhaarNumber.substring(4, 8) + DoctorConstants.EMPTY_SPACE +
                        aadhaarNumber.substring(8, 12);
                if (doctorRepository.findByAadhaarNumber(formattedAadhaarNumber).isPresent()) {
                    throw new CustomException(messageUtil.getMessage(MessageConstants.DOCTOR_AADHAAR_ALREADY_PRESENT));
                }
                existingDoctor.setAadhaarNumber(formattedAadhaarNumber);
            }
            doctorRepository.save(existingDoctor);
            return DoctorDto.builder()
                    .id(existingDoctor.getId())
                    .firstName(existingDoctor.getFirstName())
                    .lastName(existingDoctor.getLastName())
                    .department(existingDoctor.getDepartment())
                    .aadhaarNumber(existingDoctor.getAadhaarNumber())
                    .build();
        } else {
            throw new ResourceNotFoundException(messageUtil.getMessage(MessageConstants.DOCTOR_UNAVAILABLE));
        }
    }
}
