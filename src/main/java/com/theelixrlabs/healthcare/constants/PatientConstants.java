package com.theelixrlabs.healthcare.constants;

/**
 * Contains the string constants used for the patient module implementation
 */
public class PatientConstants {
    public static final String AADHAAR_FORMAT_PATTERN = "%s %s %s";
    public static final String AADHAAR_NUMBER_EXISTS_KEY = "patientAadhaarNumber.exists";
    public static final String AADHAAR_NUMBER_REGEX = "^[2-9][0-9]{11}";
    public static final String ALPHA_CHARACTERS_REGEX = "^[a-zA-Z]+$";
    public static final String CREATE_PATIENT_ENDPOINT = "/addPatient";
    public static final String FIRST_NAME_NOT_EMPTY_KEY = "firstName.not.empty";
    public static final String INVALID_FIRST_NAME_KEY = "firstName.invalid";
    public static final String INVALID_LAST_NAME_KEY = "lastName.invalid";
    public static final String INVALID_UUID_KEY = "uuid.invalid";
    public static final String LAST_NAME_SHOULD_NOT_BE_EMPTY_KEY = "lastName.not.empty";
    public static final String MESSAGE_RESOURCE_CLASSPATH_NAME = "classpath:messages";
    public static final String PATIENTS_COLLECTION_NAME = "patients";
    public static final String PATIENT_BY_ID_ENDPOINT = "/patient/{patientId}";
    public static final String PATIENT_DELETE_SUCCESS_MESSAGE = "patient.delete.success.message";
    public static final String PATIENT_DELETION_FAILED_ASSIGNED_TO_DOCTOR = "patient.deletion.failed.assigned.to.doctor";
    public static final String PATIENT_NOT_FOUND_KEY = "patient.not.found";
}
