package com.theelixrlabs.healthcare.model;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document(collection = DoctorConstants.DOCTOR_COLLECTION_NAME)
public class Doctor {
    @Id
    private String uuid = UUID.randomUUID().toString();
    private String firstName;
    private String lastName;
    private String department;
    private long aadhaarNumber;

}
