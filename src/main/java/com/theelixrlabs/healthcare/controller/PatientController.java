package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dto.PatientDTO;
import com.theelixrlabs.healthcare.exceptionhandler.CustomException;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import com.theelixrlabs.healthcare.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * Patient Controller layer, which interacts with the user.
 * All the requests will be handled by PatientController.
 * Patient Controller sends the responses to the user.
 */
@RestController
public class PatientController {

    private final PatientService patientService;
    private final MessageSource messageSource;

    //Constructor Injection of PatientService and MessageSource
    public PatientController(PatientService patientService, MessageSource messageSource) {
        this.patientService = patientService;
        this.messageSource = messageSource;
    }

    /**
     * Controller method for handling POST requests to create a new patient.
     *
     * @param patientDTO The data transfer object (DTO) containing patient information.
     * @return ResponseEntity containing a SuccessResponse with the created PatientDTO and HTTP status 201 (created).
     * @throws CustomException if validation fails or if there are errors during patient creation.
     */
    @PostMapping(PatientConstants.CREATE_PATIENT_ENDPOINT)
    public ResponseEntity<SuccessResponse<PatientDTO>> addPatientDetails(@RequestBody @Valid PatientDTO patientDTO) throws CustomException {
        return new ResponseEntity<>(new SuccessResponse<>(true, patientService.addPatientDetails(patientDTO)), HttpStatus.CREATED);
    }

    /**
     * Controller method for handling GET requests to get a patient by ID
     *
     * @param id Patient ID as String
     * @return ResponseEntity containing a SuccessResponse with the Patient Associated with the ID and HTTP status 200 (OK)
     * @throws CustomException if validation fails or if there are errors during getting patient through ID.
     */
    @GetMapping(PatientConstants.GET_PATIENT_BY_ID_ENDPOINT)
    public ResponseEntity<?> getPatientById(@PathVariable String id) throws CustomException {
        UUID patientId;
        try {
            patientId = UUID.fromString(id);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new CustomException(PatientConstants.INVALID_UUID_KEY,messageSource);
        } catch (Exception exception) {
            throw new CustomException(exception.getMessage());
        }
        return new ResponseEntity<>(new SuccessResponse<>(true, patientService.getPatientById(patientId)), HttpStatus.OK);
    }
}
