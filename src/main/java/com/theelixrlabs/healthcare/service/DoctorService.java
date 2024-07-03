package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.model.DoctorModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
        String aadhaarNumber = doctorDto.getAadhaarNumber();
        if (doctorRepository.findByAadhaarNumber(aadhaarNumber).isPresent()) {
            throw new CustomException(DoctorConstants.AADHAAR_ALREADY_PRESENT);
        }
        String formattedAadhaarNumber = aadhaarNumber.substring(0, 4) + " " +
                aadhaarNumber.substring(4, 8) + " " +
                aadhaarNumber.substring(8, 12);
        UUID uuid = UUID.randomUUID();
        DoctorModel doctorModel = DoctorModel.builder()
                .id(uuid).firstName(doctorDto.getFirstName())
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
     * Custom validation method for checking regex pattern of firstname lastname and Aadhaar number
     *
     * @param doctorDto : DoctorDto object containing doctor information.
     * @throws CustomException : Class to handle custom exception
     */
    private void validateDoctor(DoctorDto doctorDto) throws CustomException {
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

    /**
     * @param doctorId UUID of the doctor.
     * @return DoctorDto object containing doctor information.
     * @throws CustomException : Class to handle custom exception
     */
    public DoctorDto getDoctorById(UUID doctorId) throws CustomException {
        Optional<DoctorModel> doctorModelOptional = doctorRepository.findById(doctorId);
        if (doctorModelOptional.isEmpty()) {
            throw new CustomException(MessageConstants.DOCTOR_NOT_FOUND);
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
}
