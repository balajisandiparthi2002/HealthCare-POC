package com.theelixrlabs.healthcare.dao;

import com.theelixrlabs.healthcare.model.DoctorPatientAssignmentModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing DoctorPatientAssignmentModel entities in MongoDB.
 */
@Repository
public interface DoctorPatientAssignmentRepository extends MongoRepository<DoctorPatientAssignmentModel, UUID> {

    /**
     * Finds all doctor-patient assignments associated with a given patient ID.
     *
     * @param patientId The UUID of the patient to find assignments for.
     * @return A list of DoctorPatientAssignmentModel objects associated with the patient ID.
     */
    Optional<DoctorPatientAssignmentModel> findByPatientIdAndDateOfUnassignmentNull(UUID patientId);

    /**
     * Finds all doctor-patient assignments associated with a given doctor ID.
     *
     * @param validDoctorId    The UUID of the doctor to retrieve assignments for.
     * @return A list of DoctorPatientAssignmentModel objects representing assignments of the doctor to patients.
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
     * @param doctorId  UUID of the doctor associated with the assignment.
     * @param patientId UUID of the patient associated with the assignment.
     * @return true : if exists , false : if doesn't exists
     */
    boolean existsByDoctorIdAndPatientId(UUID doctorId, UUID patientId);
}
