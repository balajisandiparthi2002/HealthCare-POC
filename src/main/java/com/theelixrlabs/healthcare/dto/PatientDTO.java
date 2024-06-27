package com.theelixrlabs.healthcare.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO class for Patient
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    private UUID id;

    @NotNull(message = "{patientFirstName.notnull}")
    private String patientFirstName;

    @NotNull(message = "{patientLastName.notnull}")
    private String patientLastName;

    @NotNull(message = "{patientAadhaarNumber.notnull}")
    private Long patientAadhaarNumber;
}
