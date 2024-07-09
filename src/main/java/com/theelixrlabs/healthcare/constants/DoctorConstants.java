package com.theelixrlabs.healthcare.constants;

/**
 * Constant class containing error messages and patterns related to doctor.
 */
public class DoctorConstants {
    public static final String AADHAAR_REGEX_PATTERN = "^[2-9]{1}[0-9]{11}$";
    public static final String ALPHA_CHARACTERS_REGEX = "^[a-zA-Z]+$";
    public static final String COMMA_DELIMITER = ", ";
    public static final String CREATE_DOCTOR_END_POINT = "/saveDoctors";
    public static final String DOCTORS_COLLECTION_NAME = "doctors";
    public static final String EMPTY_SPACE = " ";
    public static final String DOCTOR_BY_ID_ENDPOINT = "/doctor/{doctorId}";
    public static final String PATH_VARIABLE_DOCTOR_ID = "doctorId";
}
