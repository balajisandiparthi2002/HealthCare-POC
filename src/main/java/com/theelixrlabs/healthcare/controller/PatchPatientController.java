package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.ApiPathsConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import com.theelixrlabs.healthcare.service.PatchPatientService;
import com.theelixrlabs.healthcare.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class PatchPatientController {

    private final PatchPatientService patchPatientService;
    private final Validator validator;

    public PatchPatientController(PatchPatientService patchPatientService, Validator validator) {
        this.patchPatientService = patchPatientService;
        this.validator = validator;
    }

    /**
     * Controller method for handling incoming Patch request.
     *
     * @param patientId  Patient id as string.
     * @param patientDto Data transfer object containing patient information.
     * @return ResponseEntity containing success response alone with modified patient details.
     */
    @PatchMapping(ApiPathsConstants.PATIENT_BY_ID_ENDPOINT)
    public ResponseEntity<SuccessResponse<PatientDto>> patchPatientById(@PathVariable String patientId, @RequestBody PatientDto patientDto) {
        PatientDto updatedPatient = patchPatientService.patchPatientById(patientId, patientDto);
        return new ResponseEntity<>(new SuccessResponse<>(true, updatedPatient), HttpStatus.OK);
    }
}
