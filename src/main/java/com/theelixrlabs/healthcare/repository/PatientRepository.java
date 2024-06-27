package com.theelixrlabs.healthcare.repository;

import com.theelixrlabs.healthcare.model.PatientModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository layer of Patient module which will interact with the database
 */
public interface PatientRepository extends MongoRepository<PatientModel, UUID> {
    /**
     * Checks for any document with specified aadhaarNumber present in db
     *
     * @param patientAadhaarNumber Patient Aadhaar Number as String
     * @return PatientModel object
     */
    Optional<PatientModel> findByPatientAadhaarNumber(String patientAadhaarNumber);
}
