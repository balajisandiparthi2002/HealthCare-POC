package com.theelixrlabs.healthcare.repository;

import com.theelixrlabs.healthcare.dto.AssignedDoctorDto;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for retrieving doctors assigned to a patient by patient ID.
 */
public interface DoctorsByPatientIdRepository {
    /**
     * Retrieves a list of assigned doctors for a given patient ID.
     *
     * @param patientId    The unique identifier of the patient.
     * @return A list of AssignedDoctorDto objects representing assigned doctors.
     */
    List<AssignedDoctorDto> getDoctorsByPatientId(UUID patientId);
}
