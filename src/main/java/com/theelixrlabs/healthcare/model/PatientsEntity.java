package com.theelixrlabs.healthcare.model;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * Entity class of Patients
 */
@AllArgsConstructor
@Data
@Document(collection = PatientConstants.PATIENTS_COLLECTION_NAME)
public class PatientsEntity {

    @Id
    private UUID id;

    @NotNull(message = PatientConstants.FIRST_NAME_MISSING_MESSAGE)
    private String patientFirstName;

    @NotNull(message = PatientConstants.LAST_NAME_MISSING_MESSAGE)
    private String patientLastName;

    @NotNull(message = PatientConstants.AADHAAR_NUMBER_MISSING_MESSAGE)
    private String patientAadhaarNumber;

    public PatientsEntity() {
        this.id = UUID.randomUUID();
    }
}
