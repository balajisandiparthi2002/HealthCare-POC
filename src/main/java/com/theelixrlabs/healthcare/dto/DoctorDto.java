package com.theelixrlabs.healthcare.dto;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Data transfer object representing doctor information.
 */
@Getter
@Setter
@NoArgsConstructor
public class DoctorDto {

    private UUID id;
    @NotBlank(message = DoctorConstants.FIRST_NAME_SHOULD_BE_MANDATORY)
    @Pattern(regexp = "^[a-zA-Z]+$", message = DoctorConstants.ONLY_CHARACTER)
    private String firstName;
    @NotBlank(message = DoctorConstants.LAST_NAME_SHOULD_BE_MANDATORY)
    @Pattern(regexp = "^[a-zA-Z]+$", message = DoctorConstants.ONLY_CHARACTER)
    private String lastName;
    @NotBlank(message = DoctorConstants.DEPARTMENT_SHOULD_BE_MANDATORY)
    @Pattern(regexp = "^[a-zA-Z]+$", message = DoctorConstants.ONLY_CHARACTER)
    private String department;
    @NotNull(message = DoctorConstants.AADHAAR_SHOULD_BE_MANDATORY)
    @Pattern(regexp = "^[2-9]{1}[0-9]{11}$", message = DoctorConstants.AADHAAR_NUMBER_PATTERN)
    private String aadhaar;
}
