package com.theelixrlabs.healthcare.dto;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * DTO class for Patient
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class PatientDTO {

    private UUID id;

    @NotNull(message = "{patientFirstName.notnull}")
    private String patientFirstName;

    @NotNull(message = "{patientLastName.notnull}")
    private String patientLastName;

    @NotNull(message = "{patientAadhaarNumber.notnull}")
    @Pattern(regexp = PatientConstants.AADHAAR_NUMBER_REGEX, message = "{patientAadhaarNumber.invalid}")
    private String patientAadhaarNumber;
}
