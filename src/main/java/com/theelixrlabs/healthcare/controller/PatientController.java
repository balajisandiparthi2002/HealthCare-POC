package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dto.PatientDTO;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import com.theelixrlabs.healthcare.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Patient Controller layer, which interacts with the user.
 * All the requests will be handled by PatientController.
 * Patient Controller sends the responses to the user.
 */
@RestController
public class PatientController {
    @Autowired
    private PatientService patientService;

    /**
     * Controller method for handling POST requests to create a new patient.
     *
     * @param patientDTO The data transfer object (DTO) containing patient information.
     * @return containing a SuccessResponse with the created PatientDTO and HTTP status 201(created).
     */
    @PostMapping(PatientConstants.CREATE_PATIENT_ENDPOINT)
    public ResponseEntity<SuccessResponse<PatientDTO>> createPatient(@RequestBody @Valid PatientDTO patientDTO) {
        SuccessResponse<PatientDTO> successResponse = new SuccessResponse<>();
        successResponse.setSuccess(true);
        successResponse.setResponseData(patientService.createPatient(patientDTO));
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
    }
}
