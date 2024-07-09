package com.theelixrlabs.healthcare.repository;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.model.DoctorModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends MongoRepository<DoctorModel, UUID> {

    Optional<DoctorModel> findByAadhaarNumber(String AadhaarNumber);

    @Query(DoctorConstants.SEARCH_DOCTOR_BY_NAME_QUERY)
    List<DoctorModel> searchByDoctorName(String doctorName);
}
