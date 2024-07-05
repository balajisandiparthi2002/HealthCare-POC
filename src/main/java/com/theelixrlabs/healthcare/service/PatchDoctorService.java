package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.model.DoctorModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service class for facilitating partial updates to doctor details.
 */
@Service
public class PatchDoctorService {

    private final DoctorRepository doctorRepository;

    public PatchDoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
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
        Optional<DoctorModel> optionalDoctor = doctorRepository.findById(doctorId);
        validateDoctor(doctorDto);
        if (optionalDoctor.isPresent()) {
            DoctorModel existingDoctor = optionalDoctor.get();
            if (doctorDto.getFirstName() != null) {
                existingDoctor.setFirstName(doctorDto.getFirstName());
            }
            if (doctorDto.getLastName() != null) {
                existingDoctor.setLastName(doctorDto.getLastName());
            }
            if (doctorDto.getDepartment() != null) {
                existingDoctor.setDepartment(doctorDto.getDepartment());
            }
            if (doctorDto.getAadhaarNumber() != null) {
                String aadhaarNumber = doctorDto.getAadhaarNumber();
                String formattedAadhaarNumber = aadhaarNumber.substring(0, 4) + " " +
                        aadhaarNumber.substring(4, 8) + " " +
                        aadhaarNumber.substring(8, 12);
                if (doctorRepository.findByAadhaarNumber(formattedAadhaarNumber).isPresent()) {
                    throw new CustomException(DoctorConstants.AADHAAR_ALREADY_PRESENT);
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
            throw new CustomException(DoctorConstants.DOCTOR_NOT_FOUND);
        }
    }

    private void validateDoctor(DoctorDto doctorDto) throws CustomException {
        if (doctorDto.getFirstName() != null) {
            if (doctorDto.getFirstName().isEmpty()) {
                throw new CustomException(DoctorConstants.FIRST_NAME_SHOULD_NOT_EMPTY);
            } else if (!doctorDto.getFirstName().matches(DoctorConstants.CHARACTER_ONLY_REGEX_PATTERN)) {
                throw new CustomException(DoctorConstants.INVALID_FIRSTNAME);
            }
        }
        if (doctorDto.getLastName() != null) {
            if (doctorDto.getLastName().isEmpty()) {
                throw new CustomException(DoctorConstants.LAST_NAME_SHOULD_NOT_EMPTY);
            } else if (!doctorDto.getLastName().matches(DoctorConstants.CHARACTER_ONLY_REGEX_PATTERN)) {
                throw new CustomException(DoctorConstants.INVALID_LASTNAME);
            }
        }
        if (doctorDto.getDepartment() != null) {
            if (doctorDto.getLastName().isEmpty()) {
                throw new CustomException(DoctorConstants.LAST_NAME_SHOULD_NOT_EMPTY);
            }
        }
        if (doctorDto.getAadhaarNumber() != null) {
            if (doctorDto.getAadhaarNumber().isEmpty()) {
                throw new CustomException(DoctorConstants.AADHAAR_SHOULD_NOT_EMPTY);
            } else if (!(doctorDto.getAadhaarNumber().matches(DoctorConstants.AADHAAR_REGEX_PATTERN))) {
                throw new CustomException(DoctorConstants.INVALID_AADHAAR_NUMBER);
            }
        }
    }
}
