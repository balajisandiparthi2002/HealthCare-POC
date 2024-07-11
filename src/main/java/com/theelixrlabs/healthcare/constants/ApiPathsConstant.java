package com.theelixrlabs.healthcare.constants;

/**
 * Contains endpoint constants used throughout the healthcare application.
 */
public class ApiPathsConstant {
    public static final String ASSIGN_DOCTOR_TO_PATIENT_URL = "/assignDoctorToPatient";
    public static final String CREATE_DOCTOR_END_POINT = "/saveDoctors";
    public static final String CREATE_PATIENT_ENDPOINT = "/addPatient";
    public static final String GET_DOCTOR_BY_ID_ENDPOINT = "/doctors/{doctorId}";
    public static final String GET_DOCTORS_BY_NAME_ENDPOINT = "/getDoctorsByName";
    public static final String GET_PATIENTS_BY_NAME_ENDPOINT = "/getPatientsByName";
    public static final String PATCH_DOCTOR_ENDPOINT = "/patchDoctor/{doctorId}";
    public static final String PATIENT_BY_ID_ENDPOINT = "/patient/{patientId}";
    public static final String UNASSIGN_DOCTOR_FROM_PATIENT_URL = "/unassignDoctorFromPatient";
}
