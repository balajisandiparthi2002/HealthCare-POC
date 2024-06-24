package com.theelixrlabs.healthcare.model;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * Entity class of Patient
 */
@AllArgsConstructor
@Data
@Document(collection = "patient")
public class Patient {

    @Id
    private String uuId;

    @NotNull(message = PatientConstants.MANDATORY_FIELD_MESSAGE)
    private String patientFirstName;

    @NotNull(message = PatientConstants.MANDATORY_FIELD_MESSAGE)
    private String patientLastName;

    @NotNull(message = PatientConstants.MANDATORY_FIELD_MESSAGE)
    private String patientAadhaarNumber;

    public Patient() {
        this.uuId = UUID.randomUUID().toString();
    }
}
