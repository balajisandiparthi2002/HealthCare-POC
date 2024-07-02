package com.theelixrlabs.healthcare.model;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.UUID;

/**
 * Entity class representing doctors in the healthcare system.
 * This class is mapped to the MongoDB collection specified by DoctorConstants.DOCTOR_COLLECTION_NAME.
 */
@Data
@Document(collection = DoctorConstants.DOCTORS_COLLECTION_NAME)
public class DoctorsEntity {
    @Id
    private UUID id;
    @NotBlank(message = MessageConstants.FIRST_NAME_SHOULD_BE_MANDATORY)
    private String firstName;
    @NotBlank(message = MessageConstants.LAST_NAME_SHOULD_BE_MANDATORY)
    private String lastName;
    @NotBlank(message = MessageConstants.DEPARTMENT_SHOULD_BE_MANDATORY)
    private String department;
    @NotBlank(message = MessageConstants.AADHAAR_NUMBER_SHOULD_BE_MANDATORY)
    @Pattern(regexp = DoctorConstants.AADHAAR_NUMBER_PATTERN, message = MessageConstants.INVALID_AADHAAR_NUMBER_FORMAT)
    private String aadhaarNumber;

    /**
     * Constructor to initialize a new DoctorsEntity object with a randomly generated UUID as ID.
     */
    public DoctorsEntity() {
        this.id = UUID.randomUUID();
    }
}
