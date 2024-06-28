package com.theelixrlabs.healthcare.model;

import com.theelixrlabs.healthcare.constants.PatientConstants;
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
public class PatientModel {

    @Id
    private UUID id;
    private String patientFirstName;
    private String patientLastName;
    private String patientAadhaarNumber;

    public PatientModel() {
        this.id = UUID.randomUUID();
    }
}
