package com.theelixrlabs.healthcare.repository;

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
     * Retrieves an active assignment for a patient identified by the provided patientId,
     * where the date of unassignment is null.
     *
     * @param validPatientId    The UUID identifying the patient for whom to retrieve the active assignment.
     * @return An Optional containing the DoctorPatientAssignmentModel if found,
     * or an empty Optional if no active assignment is found.
     */
    Optional<DoctorPatientAssignmentModel> findByPatientIdAndDateOfUnassignmentNull(UUID validPatientId);

    /**
     * Retrieves an active assignment for a doctor identified by the provided validDoctorId,
     * where the date of unassignment is null.
     *
     * @param validDoctorId    The UUID identifying the doctor for whom to retrieve the active assignment.
     * @return An Optional containing the DoctorPatientAssignmentModel if found,
     * or an empty Optional if no active assignment is found.
     */
    Optional<DoctorPatientAssignmentModel> findByDoctorIdAndDateOfUnassignmentNull(UUID validDoctorId);

    /**
     * Method declaration to find a DoctorPatientAssignmentModel by DoctorID and PatientID and date of unassignment not null
     *
     * @param doctorId    UUID of the doctor associated with the assignment.
     * @param patientId    UUID of the patient associated with the assignment.
     * @return Optional containing DoctorPatientAssignmentModel if found, empty otherwise.
     */
    Optional<DoctorPatientAssignmentModel> findByDoctorIdAndPatientIdAndDateOfUnassignmentNull(UUID doctorId, UUID patientId);

    /**
     * Method declaration to check for a DoctorPatientAssignment by DoctorID and PatientID
     *
     * @param doctorId    UUID of the doctor associated with the assignment.
     * @param patientId    UUID of the patient associated with the assignment.
     * @return true : if exists , false : if doesn't exists
     */
    boolean existsByDoctorIdAndPatientId(UUID doctorId, UUID patientId);
}
