package com.theelixrlabs.healthcare.constants;

/**
 * Contains the string constants used for the doctor patient assignment module implementation
 */
public class DoctorPatientAssignmentConstants {
    public static final String ASSIGN_DOCTOR_TO_PATIENT_URL = "/assignDoctorToPatient";
    public static final String DB_COLLECTION_NAME = "doctor_patient_assignment";
    public static final String DOCTOR_ALREADY_ASSIGNED_KEY = "doctor.already.assigned";
    public static final String DOCTOR_ALREADY_UNASSIGNED_KEY = "doctor.already.unassigned";
    public static final String DOCTOR_ID_INVALID_KEY = "doctorId.invalid";
    public static final String DOCTOR_NOT_FOUND_KEY = "doctorId.notFound";
    public static final String NO_ASSIGNMENT_EXISTS_KEY = "assignment.not.exists";
    public static final String PATIENT_ID_INVALID_KEY = "patientId.invalid";
    public static final String PATIENT_NOT_FOUND_KEY = "patientId.notFound";
    public static final String UNASSIGN_DOCTOR_FROM_PATIENT_URL = "/unassignDoctorFromPatient";
}
