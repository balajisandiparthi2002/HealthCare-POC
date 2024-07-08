package com.theelixrlabs.healthcare.constants;

/**
 * Defines constants for validation message keys used in the healthcare system.
 * These keys are mapped to actual messages in the properties file.
 */
public class MessageConstants {
    public static final String AADHAAR_ALREADY_PRESENT = "doctor.aadhaarNumber.already.present";
    public static final String AADHAAR_NUMBER_SHOULD_BE_MANDATORY = "{doctor.aadhaarNumber.mandatory}";
    public static final String AADHAAR_SHOULD_NOT_BE_EMPTY = "doctor.aadhaarNumber.should.not.be.empty";
    public static final String DEPARTMENT_SHOULD_BE_MANDATORY = "{doctor.department.mandatory}";
    public static final String DOCTOR_ID_IS_MANDATORY = "{validation.doctorID.mandatory}";
    public static final String DOCTOR_NAME_CANNOT_BE_EMPTY = "doctor.name.cannot.be.empty";
    public static final String DOCTOR_UNAVAILABLE = "doctor.unavailable";
    public static final String FIRST_NAME_SHOULD_BE_MANDATORY = "{doctor.firstName.mandatory}";
    public static final String FIRST_NAME_SHOULD_NOT_BE_EMPTY = "doctor.firstName.should.not.be.empty";
    public static final String INVALID_AADHAAR_NUMBER = "doctor.invalid.aadhaar.number";
    public static final String INVALID_FIRSTNAME = "doctor.invalid.firstName";
    public static final String INVALID_LASTNAME = "doctor.invalid.lastName";
    public static final String INVALID_UUID = "uuid.invalid";
    public static final String LAST_NAME_SHOULD_BE_MANDATORY = "{doctor.lastName.mandatory}";
    public static final String LAST_NAME_SHOULD_NOT_BE_EMPTY = "doctor.lastName.should.not.be.empty";
    public static final String NO_DOCTOR_FOUND = "doctor.not.found";
    public static final String PATIENT_ID_IS_MANDATORY = "{validation.patientID.mandatory}";
}
