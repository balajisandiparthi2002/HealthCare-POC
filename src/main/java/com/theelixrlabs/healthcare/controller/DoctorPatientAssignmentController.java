package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.DoctorPatientAssignmentConstants;
import com.theelixrlabs.healthcare.dto.DoctorPatientAssignmentDTO;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import com.theelixrlabs.healthcare.service.DoctorPatientAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DoctorPatientAssignmentController {

    private final DoctorPatientAssignmentService doctorPatientAssignmentService;

    public DoctorPatientAssignmentController(DoctorPatientAssignmentService doctorPatientAssignmentService) {
        this.doctorPatientAssignmentService = doctorPatientAssignmentService;
    }

    /**
     * End point to create assign doctor to patient based on provided dto.
     *
     * @param doctorPatientAssignmentDTO : DTO object containing doctorId and patientId.
     * @return ResponseEntity containing a success response with created dto.
     */
    @PostMapping(DoctorPatientAssignmentConstants.ASSIGN_DOCTOR_TO_PATIENT_URL)
    public ResponseEntity<SuccessResponse<DoctorPatientAssignmentDTO>> assignDoctorToPatient(@Valid @RequestBody DoctorPatientAssignmentDTO doctorPatientAssignmentDTO) throws CustomException {
        return new ResponseEntity<>(new SuccessResponse<>(true, doctorPatientAssignmentService.assignDoctorToPatient(doctorPatientAssignmentDTO)), HttpStatus.CREATED);
    }

}
