package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dao.PatientRepository;
import com.theelixrlabs.healthcare.dto.PatientDTO;
import com.theelixrlabs.healthcare.model.PatientModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing patient-related operations.
 * Validations on the input parameters are done in this class.
 */
@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    /**
     * Creates a new patient based on the provided PatientDTO.
     *
     * @param patientDTO    The data transfer object containing patient information.
     * @return The PatientDTO of the newly created patient, with ID populated.
     * @throws RuntimeException If the aadhaar number already exists in the database.
     */
    public PatientDTO createPatient(PatientDTO patientDTO) throws RuntimeException {
        //check if aadhaar number already exists.
        if (patientRepository.findByAadhaarNumber(patientDTO.getAadhaarNumber()).isPresent()) {
            throw new RuntimeException(PatientConstants.AADHAAR_NUMBER_ALREADY_EXISTS);
        }

        //mapping PatientDTO to PatientModel
        PatientModel patientModel = new PatientModel();
        patientModel.setFirstName(patientDTO.getFirstName());
        patientModel.setLastName(patientDTO.getLastName());
        patientModel.setAadhaarNumber(patientDTO.getAadhaarNumber());

        //save PatientModel to the database
        patientRepository.save(patientModel);

        //Populate the ID generated by the database back to PatientDTO
        patientDTO.setId(patientModel.getId());
        return patientDTO;
    }
}
