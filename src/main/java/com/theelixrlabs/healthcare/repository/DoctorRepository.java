package com.theelixrlabs.healthcare.repository;

import com.theelixrlabs.healthcare.model.DoctorModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends MongoRepository<DoctorModel, UUID> {

    Optional<DoctorModel> findByAadhaarNumber(String AadhaarNumber);
}
