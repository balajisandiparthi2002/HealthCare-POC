package com.theelixrlabs.healthcare.repository;

import com.theelixrlabs.healthcare.model.DoctorsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DoctorRepository extends MongoRepository<DoctorsEntity, UUID> {
}
