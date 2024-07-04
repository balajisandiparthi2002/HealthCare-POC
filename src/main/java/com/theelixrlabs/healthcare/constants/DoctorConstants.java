package com.theelixrlabs.healthcare.constants;

/**
 * Constant class containing error messages and patterns related to doctor.
 */
public class DoctorConstants {
    public static final String AADHAAR_ALREADY_PRESENT = "Aadhaar already present in database";
    public static final String AADHAAR_NUMBER_PATTERN_MESSAGE = "Aadhaar number must be only 12 digits!";
    public static final String AADHAAR_REGEX_PATTERN = "^[2-9]{1}[0-9]{11}$";
    public static final String AADHAAR_SHOULD_BE_MANDATORY = "Aadhaar should be mandatory!";
    public static final String CHARACTER_ONLY_REGEX_PATTERN = "^[a-zA-Z]+$";
    public static final String COMMA_DELIMITER = ", ";
    public static final String CREATE_DOCTOR_END_POINT = "/saveDoctors";
    public static final String DEPARTMENT_SHOULD_BE_MANDATORY = "Department name should be mandatory!";
    public static final String DOCTORS_COLLECTION_NAME = "doctors";
    public static final String EMPTY_SPACE = " ";
    public static final String FIRST_NAME_MUST_BE_CHARACTER = "First name should be character only!";
    public static final String FIRST_NAME_SHOULD_BE_MANDATORY = "First name should be mandatory!";
    public static final String GET_DOCTOR_BY_ID_ENDPOINT = "/getDoctorById/{doctorId}";
    public static final String LAST_NAME_MUST_BE_CHARACTER = "Last name should be character only!";
    public static final String LAST_NAME_SHOULD_BE_MANDATORY = "Last name should be mandatory!";
    public static final String PATH_VARIABLE_DOCTOR_ID = "doctorId";
}
