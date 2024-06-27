package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.modal.DoctorEntity;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling operations related to doctor.
 */
@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * Saves a new doctor document based on the provided DoctorDto.
     *
     * @param doctorDto : DoctorDto object containing doctor information.
     * @return The saved dto object.
     */
    public DoctorDto saveDoctor(DoctorDto doctorDto) throws RuntimeException {
        if (doctorRepository.findByAadhaar(doctorDto.getAadhaar()).isPresent()) {
            throw new RuntimeException(DoctorConstants.AADHAAR_ALREADY_PRESENT);
        }
        DoctorEntity doctorEntity = new DoctorEntity();
        doctorEntity.setFirstName(doctorDto.getFirstName());
        doctorEntity.setLastName(doctorDto.getLastName());
        doctorEntity.setDepartment(doctorDto.getDepartment());
        doctorEntity.setAadhaar(doctorDto.getAadhaar());
        doctorRepository.save(doctorEntity);
        doctorDto.setId(doctorEntity.getId());
        return doctorDto;
    }
}
