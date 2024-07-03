package com.theelixrlabs.healthcare.model;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

/**
 * Entity class representing doctors in the healthcare system.
 * This class is mapped to the MongoDB collection specified by DoctorConstants.DOCTOR_COLLECTION_NAME.
 */
@Data
@Document(collection = DoctorConstants.DOCTORS_COLLECTION_NAME)
@SuperBuilder
public class DoctorModel {
    @Id
    private UUID id;

    @NotBlank(message = MessageConstants.FIRST_NAME_SHOULD_BE_MANDATORY)
    private String firstName;

    @NotBlank(message = MessageConstants.LAST_NAME_SHOULD_BE_MANDATORY)
    private String lastName;

    @NotBlank(message = MessageConstants.DEPARTMENT_SHOULD_BE_MANDATORY)
    private String department;

    @NotBlank(message = MessageConstants.AADHAAR_NUMBER_SHOULD_BE_MANDATORY)
    private String aadhaarNumber;
}
