package com.theelixrlabs.healthcare.dto;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Data transfer object representing doctor information.
 */
@NoArgsConstructor
@Data
@SuperBuilder
public class DoctorDto {
    private UUID id;

    @NotBlank(message = DoctorConstants.FIRST_NAME_SHOULD_BE_MANDATORY)
    private String firstName;

    @NotBlank(message = DoctorConstants.LAST_NAME_SHOULD_BE_MANDATORY)
    private String lastName;

    @NotBlank(message = DoctorConstants.DEPARTMENT_SHOULD_BE_MANDATORY)
    private String department;

    @NotNull(message = DoctorConstants.AADHAAR_SHOULD_BE_MANDATORY)
    private String aadhaarNumber;
}
