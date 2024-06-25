package com.theelixrlabs.healthcare.repository;

import com.theelixrlabs.healthcare.model.PatientsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.UUID;

/**
 * Repository layer of Patient module which will interact with the database
 */
public interface PatientRepository extends MongoRepository<PatientsEntity, UUID> {
    /**
     * Checks if the aadhaar number is present in the database
     *
     * @param aadhaarNumber aadhaar number as string
     * @return true : if aadhaar number exists , false : if doesn't exists
     */
    @Query(value = "{ 'aadhaarNumber': ?0 }", exists = true)
    boolean isAadhaarNumberExists(String aadhaarNumber);
}
