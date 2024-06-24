package com.theelixrlabs.healthcare.repository;

import com.theelixrlabs.healthcare.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PatientRepository extends MongoRepository<Patient, Integer> {
    /**
     * Checks if the aadhaar number is present in the database
     *
     * @param aadhaarNumber aadhaar number as string
     * @return true : if aadhaar number exists , false : if doesn't exists
     */
    @Query(value = "{ 'aadhaarNumber': ?0 }", exists = true)
    boolean isAadhaarNumberExists(String aadhaarNumber);
}
