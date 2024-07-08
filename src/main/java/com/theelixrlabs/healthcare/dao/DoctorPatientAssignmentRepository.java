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
     * Retrieves a list of DoctorPatientAssignmentModel objects based on the provided doctorId and patientId.
     *
     * @param doctorId The UUID of the doctor associated with the assignments to search for.
     * @param patientId The UUID of the patient associated with the assignments to search for.
     * @return A list of DoctorPatientAssignmentModel objects that match both the given doctorId and patientId.
     */
    List<DoctorPatientAssignmentModel> findByDoctorIdAndPatientId(UUID doctorId, UUID patientId);

    /**
     * Finds all doctor-patient assignments associated with a given patient ID.
     *
     * @param patientId    The UUID of the patient to find assignments for.
     * @return A list of DoctorPatientAssignmentModel objects associated with the patient ID.
     */
    List<DoctorPatientAssignmentModel> findByPatientId(UUID patientId);
}
