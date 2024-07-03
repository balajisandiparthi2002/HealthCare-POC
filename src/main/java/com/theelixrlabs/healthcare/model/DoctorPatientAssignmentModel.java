package com.theelixrlabs.healthcare.model;

import com.theelixrlabs.healthcare.constants.MessageConstants;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder
@Document(collection = MessageConstants.DB_COLLECTION_NAME)
public class DoctorPatientAssignmentModel {
    @Id
    private UUID id;
    @NotNull(message = MessageConstants.DOCTOR_ID_IS_MANDATORY)
    private UUID doctorID;
    @NotNull(message = MessageConstants.PATIENT_ID_IS_MANDATORY)
    private UUID patientID;
    private Date dateOfAssignment;
    private Date dateOfUnassignment;
}
