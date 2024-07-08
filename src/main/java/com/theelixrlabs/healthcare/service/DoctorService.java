package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.model.DoctorModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class responsible for handling operations related to doctor.
 */
@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final MessageUtil messageUtil;

    public DoctorService(DoctorRepository doctorRepository, MessageUtil messageUtil) {
        this.doctorRepository = doctorRepository;
        this.messageUtil = messageUtil;
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

        String formattedAadhaarNumber = aadhaarNumber.substring(0, 4) + DoctorConstants.EMPTY_SPACE +
                aadhaarNumber.substring(4, 8) + DoctorConstants.EMPTY_SPACE +
                aadhaarNumber.substring(8, 12);
        if (doctorRepository.findByAadhaarNumber(formattedAadhaarNumber).isPresent()) {
            throw new CustomException(messageUtil.getMessage(MessageConstants.AADHAAR_ALREADY_PRESENT));
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
     * Custom validation method for checking regex pattern of firstname lastname and Aadhaar number
     *
     * @param doctorDto : DoctorDto object containing doctor information.
     * @throws CustomException : Class to handle custom exception
     */
    private void validateDoctor(DoctorDto doctorDto) throws CustomException {

        if (doctorDto.getFirstName().isEmpty()) {
            throw new CustomException(messageUtil.getMessage(MessageConstants.FIRST_NAME_SHOULD_NOT_EMPTY));
        } else if (!(doctorDto.getFirstName().matches(DoctorConstants.CHARACTER_ONLY_REGEX_PATTERN))) {
            throw new CustomException(messageUtil.getMessage(MessageConstants.INVALID_FIRSTNAME));
        }
        if (doctorDto.getLastName().isEmpty()) {
            throw new CustomException(messageUtil.getMessage(MessageConstants.LAST_NAME_SHOULD_NOT_EMPTY));
        } else if (!(doctorDto.getLastName().matches(DoctorConstants.CHARACTER_ONLY_REGEX_PATTERN))) {
            throw new CustomException(messageUtil.getMessage(MessageConstants.INVALID_LASTNAME));
        }
        if (doctorDto.getAadhaarNumber().isEmpty()) {
            throw new CustomException(messageUtil.getMessage(MessageConstants.AADHAAR_SHOULD_NOT_EMPTY));
        } else if (!(doctorDto.getAadhaarNumber().matches(DoctorConstants.AADHAAR_REGEX_PATTERN))) {
            throw new CustomException(messageUtil.getMessage(MessageConstants.INVALID_AADHAAR_NUMBER));
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
            throw new CustomException(messageUtil.getMessage(MessageConstants.DOCTOR_UNAVAILABLE));
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
