package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.modal.DoctorEntity;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service class responsible for handling operations related to doctor.
 */
@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    /**
     * Saves a new doctor document based on the provided DoctorDto.
     *
     * @param doctorDto : DoctorDto object containing doctor information.
     * @return The saved dto object.
     */
    public DoctorDto saveDoctor(DoctorDto doctorDto) throws CustomException {
        validateDoctor(doctorDto);
        if (doctorRepository.findByAadhaarNumber(doctorDto.getAadhaarNumber()).isPresent()) {
            throw new CustomException(DoctorConstants.AADHAAR_ALREADY_PRESENT);
        }
        UUID uuid = UUID.randomUUID();
        DoctorEntity doctorEntity = DoctorEntity.builder().id(uuid).firstName(doctorDto.getFirstName()).lastName(doctorDto.getLastName()).department(doctorDto.getDepartment()).aadhaarNumber(doctorDto.getAadhaarNumber()).build();
        doctorRepository.save(doctorEntity);
        DoctorDto savedDoctorDto;
        savedDoctorDto = DoctorDto.builder().id(doctorEntity.getId()).firstName(doctorEntity.getFirstName()).lastName(doctorEntity.getLastName()).department(doctorEntity.getDepartment()).aadhaarNumber(doctorEntity.getAadhaarNumber()).build();
        return savedDoctorDto;
    }

    /**
     * Custom validation method for checking regex pattern of firstname lastname and Aadhaar number
     *
     * @param doctorDto : DoctorDto object containing doctor information.
     * @throws CustomException : Class to handle custom exception
     */
    public void validateDoctor(DoctorDto doctorDto) throws CustomException {
        if (!(doctorDto.getFirstName().matches(DoctorConstants.CHARACTER_ONLY_REGEX_PATTERN))) {
            throw new CustomException(DoctorConstants.FIRST_NAME_MUST_BE_CHARACTER);
        }
        if (!(doctorDto.getLastName().matches(DoctorConstants.CHARACTER_ONLY_REGEX_PATTERN))) {
            throw new CustomException(DoctorConstants.LAST_NAME_MUST_BE_CHARACTER);
        }
        if (!(doctorDto.getAadhaarNumber().matches(DoctorConstants.AADHAAR_REGEX_PATTERN))) {
            throw new CustomException(DoctorConstants.AADHAAR_NUMBER_PATTERN_MESSAGE);
        }
    }
}
