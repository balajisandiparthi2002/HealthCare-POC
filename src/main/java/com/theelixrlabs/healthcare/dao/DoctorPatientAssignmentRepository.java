package com.theelixrlabs.healthcare.dao;

import com.theelixrlabs.healthcare.model.DoctorPatientAssignmentModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing DoctorPatientAssignmentModel entities in MongoDB.
 */
@Repository
public interface DoctorPatientAssignmentRepository extends MongoRepository<DoctorPatientAssignmentModel, UUID> {
    /**
     * Method declaration to find a DoctorPatientAssignmentModel by DoctorID and PatientID and date of unassignment not null
     *
     * @param doctorId  UUID of the doctor associated with the assignment.
     * @param patientId UUID of the patient associated with the assignment.
     * @return Optional containing DoctorPatientAssignmentModel if found, empty otherwise.
     */
    Optional<DoctorPatientAssignmentModel> findByDoctorIdAndPatientIdAndDateOfUnassignmentNull(UUID doctorId, UUID patientId);

    /**
     * Method declaration to check for a DoctorPatientAssignment by DoctorID and PatientID
     *
     * @param doctorId  UUID of the doctor associated with the assignment.
     * @param patientId UUID of the patient associated with the assignment.
     * @return true : if exists , false : if doesn't exists
     */
    boolean existsByDoctorIdAndPatientId(UUID doctorId, UUID patientId);
}
