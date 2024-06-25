package com.theelixrlabs.healthcare.model;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * Entity class representing doctors in the healthcare system.
 * This class is mapped to the MongoDB collection specified by DoctorConstants.DOCTOR_COLLECTION_NAME.
 */
@Data
@Document(collection = DoctorConstants.DOCTOR_COLLECTION_NAME)
public class DoctorsEntity {
    @Id
    private UUID id;
    private String firstName;
    private String lastName;
    private String department;
    private long aadhaarNumber;

    /**
     * Constructor to initialize a new DoctorsEntity object with a randomly generated UUID as ID.
     */
    public DoctorsEntity() {
        this.id = UUID.randomUUID();
    }
}
