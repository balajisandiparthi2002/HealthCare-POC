package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.ApiPathsConstant;
import com.theelixrlabs.healthcare.dto.DoctorPatientAssignmentDto;
import com.theelixrlabs.healthcare.exceptionHandler.DataException;
import com.theelixrlabs.healthcare.exceptionHandler.DoctorNotFoundException;
import com.theelixrlabs.healthcare.exceptionHandler.DoctorPatientAssignmentException;
import com.theelixrlabs.healthcare.exceptionHandler.PatientNotFoundException;
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
     * @param doctorPatientAssignmentDto : DTO object containing doctorId and patientId.
     * @return ResponseEntity containing a success response with created dto.
     */
    @PostMapping(ApiPathsConstant.ASSIGN_DOCTOR_TO_PATIENT_URL)
    public ResponseEntity<SuccessResponse<DoctorPatientAssignmentDto>> assignDoctorToPatient(@Valid @RequestBody DoctorPatientAssignmentDto doctorPatientAssignmentDto) throws DoctorPatientAssignmentException, DataException, DoctorNotFoundException, PatientNotFoundException {
        return new ResponseEntity<>(new SuccessResponse<>(true, doctorPatientAssignmentService.assignDoctorToPatient(doctorPatientAssignmentDto), null), HttpStatus.OK);
    }

    /**
     * End point to  unassign doctor from patient based on provided dto.
     *
     * @param doctorPatientAssignmentDto : DTO object containing doctorId and patientId.
     * @return ResponseEntity containing a success response with created dto.
     */
    @PostMapping(ApiPathsConstant.UNASSIGN_DOCTOR_FROM_PATIENT_URL)
    public ResponseEntity<SuccessResponse<DoctorPatientAssignmentDto>> unassignDoctorFromPatient(@Valid @RequestBody DoctorPatientAssignmentDto doctorPatientAssignmentDto) throws DoctorPatientAssignmentException,DataException,DoctorNotFoundException,PatientNotFoundException{
        doctorPatientAssignmentService.unassignDoctorFromPatient(doctorPatientAssignmentDto);
        return new ResponseEntity<>(new SuccessResponse<>(true, null, null), HttpStatus.OK);
    }
}
