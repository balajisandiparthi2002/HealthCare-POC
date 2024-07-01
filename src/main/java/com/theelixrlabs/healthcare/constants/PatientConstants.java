package com.theelixrlabs.healthcare.constants;

/**
 * Contains the string constants used for the patient module implementation
 */
public class PatientConstants {
    public static final String AADHAAR_FORMAT_PATTERN = "%s %s %s";
    public static final String AADHAAR_NUMBER_REGEX = "^[2-9][0-9]{11}";
    public static final String AADHAAR_NUMBER_ALREADY_EXISTS = "Aadhaar Number already exists.";
    public static final String ALPHA_CHARACTERS_ONLY_REGEX = "^[a-zA-Z]+$";
    public static final String CREATE_PATIENT_ENDPOINT = "/addPatient";
    public static final String FIRST_NAME_SHOULD_NOT_BE_EMPTY = "FirstName should not be empty";
    public static final String GET_PATIENT_BY_ID_ENDPOINT = "/getPatientById/{id}";
    public static final String INVALID_FIRST_NAME = "Invalid FirstName";
    public static final String INVALID_LAST_NAME = "Invalid LastName";
    public static final String INVALID_UUID = "Invalid UUID";
    public static final String LAST_NAME_SHOULD_NOT_BE_EMPTY = "LastName should not be empty";
    public static final String MESSAGE_RESOURCE_CLASSPATH_NAME="classpath:messages";
    public static final String PATIENTS_COLLECTION_NAME = "patients";
    public static final String PATIENT_NOT_FOUND = "Patient Not Found";
}
