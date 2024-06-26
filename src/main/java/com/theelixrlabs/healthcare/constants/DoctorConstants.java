package com.theelixrlabs.healthcare.constants;

/**
 * Constants related to doctors in the healthcare system.
 */
public class DoctorConstants {
    public static final String DOCTORS_COLLECTION_NAME = "doctors";
    public static final String FIRST_NAME_SHOULD_BE_MANDATORY = "First name is mandatory";
    public static final String LAST_NAME_SHOULD_BE_MANDATORY = "Last name is mandatory";
    public static final String DEPARTMENT_SHOULD_BE_MANDATORY = "Department is mandatory";
    public static final String AADHAAR_NUMBER_SHOULD_BE_MANDATORY = "Aadhaar number is mandatory";
    public static final String INVALID_AADHAAR_NUMBER_FORMAT = "Aadhaar number must be exactly 12 digits and contain only numeric characters";
    public static final String AADHAAR_NUMBER_PATTERN = "^\\d{12}$";
}
