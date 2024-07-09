package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dto.PatientDTO;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import com.theelixrlabs.healthcare.service.PatientService;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * Patient Controller layer, which interacts with the user.
 * All the requests will be handled by PatientController.
 * Patient Controller sends the responses to the user.
 */
@RestController
public class PatientController {

    private final PatientService patientService;

    //Constructor Injection
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Controller method for handling POST requests to create a new patient.
     *
     * @param patientDTO    The data transfer object (DTO) containing patient information.
     * @return ResponseEntity containing a SuccessResponse with the created PatientDTO and HTTP status 201 (created).
     * @throws CustomException    if validation fails or if there are errors during patient creation.
     */
    @PostMapping(PatientConstants.CREATE_PATIENT_ENDPOINT)
    public ResponseEntity<SuccessResponse<PatientDTO>> addPatientDetails(@RequestBody @Valid PatientDTO patientDTO) throws CustomException {
        return new ResponseEntity<>(new SuccessResponse<>(true, patientService.addPatientDetails(patientDTO)), HttpStatus.CREATED);
    }

    /**
     * Controller method for handling GET requests to get a patient by ID
     *
     * @param patientId    Patient ID as String
     * @return ResponseEntity containing a SuccessResponse with the Patient Associated with the ID and HTTP status 200 (OK)
     * @throws CustomException    if validation fails or if there are errors during getting patient through ID.
     */
    @GetMapping(PatientConstants.PATIENT_BY_ID_ENDPOINT)
    public ResponseEntity<?> getPatientById(@PathVariable String patientId) throws CustomException {
        return new ResponseEntity<>(new SuccessResponse<>(true, patientService.getPatientById(patientId)), HttpStatus.OK);
    }

    /**
     * Endpoint to delete a patient by ID.
     *
     * @param patientId    The ID of the patient to delete, provided as a path variable.
     * @return ResponseEntity containing a SuccessResponse indicating success or failure.
     * @throws CustomException    if there's an issue during the deletion process.
     */
    @DeleteMapping(PatientConstants.PATIENT_BY_ID_ENDPOINT)
    public ResponseEntity<SuccessResponse<String>> deletePatientById(@PathVariable String patientId) throws CustomException {
        return new ResponseEntity<>(new SuccessResponse<>(true, patientService.deletePatientById(patientId)), HttpStatus.OK);
    }
}
