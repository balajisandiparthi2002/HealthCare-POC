package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.exceptionHandler.ResourceNotFoundException;
import com.theelixrlabs.healthcare.model.DoctorModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.validation.DoctorModelValidator;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service class responsible for handling operations related to doctor.
 */
@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorModelValidator doctorModelValidator;
    private final MessageUtil messageUtil;
    private final Validator validator;

    public DoctorService(DoctorRepository doctorRepository, DoctorModelValidator doctorModelValidator, MessageUtil messageUtil, Validator validator) {
        this.doctorRepository = doctorRepository;
        this.doctorModelValidator = doctorModelValidator;
        this.messageUtil = messageUtil;
        this.validator=validator;
    }

    /**
     * Saves a new doctor document based on the provided DoctorDto.
     *
     * @param doctorDto : DoctorDto object containing doctor information.
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

    /**
     * Searches for doctors by their name and returns a list of DoctorDto objects.
     *
     * @param doctorName doctorName the name of the doctor to search for
     * @return a list of DoctorDto objects representing the matching doctors
     */
    public List<DoctorDto> getDoctorsByName(String doctorName) {
        doctorModelValidator.validateDoctorName(doctorName);
        List<DoctorModel> doctorModelList = doctorRepository.searchByDoctorName(doctorName);
        if (doctorModelList.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage(MessageConstants.DOCTOR_NOT_FOUND));
        }
        List<DoctorDto> doctorDtoList = new ArrayList<>();
        for (DoctorModel doctorModel : doctorModelList) {
            DoctorDto doctorDto = DoctorDto.builder()
                    .id(doctorModel.getId())
                    .firstName(doctorModel.getFirstName())
                    .lastName(doctorModel.getLastName())
                    .department(doctorModel.getDepartment())
                    .aadhaarNumber(doctorModel.getAadhaarNumber())
                    .build();
            doctorDtoList.add(doctorDto);
        }
        return doctorDtoList;
    }
}
