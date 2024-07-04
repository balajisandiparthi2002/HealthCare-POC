package com.theelixrlabs.healthcare.dao;

import com.theelixrlabs.healthcare.model.DoctorPatientAssignmentModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing DoctorPatientAssignmentModel entities in MongoDB.
 */
@Repository
public interface DoctorPatientAssignmentRepository extends MongoRepository<DoctorPatientAssignmentModel, UUID> {
    /**
     * Method declaration to find a DoctorPatientAssignmentModel by DoctorID and PatientID.
     *
     * @param doctorID    UUID of the doctor associated with the assignment.
     * @param patientID    UUID of the patient associated with the assignment.
     * @return Optional containing DoctorPatientAssignmentModel if found, empty otherwise.
     */
    List<DoctorPatientAssignmentModel> findByDoctorIDAndPatientID(UUID doctorID, UUID patientID);
}
