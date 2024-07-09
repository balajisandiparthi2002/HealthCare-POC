package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.service.DoctorService;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Rest Controller for handling HTTP request and response.
 */
@RestController
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * End point to create new doctor based on provided dto.
     *
     * @param doctorDto : DTO object containing doctor object.
     * @return ResponseEntity containing a success response with created dto.
     */
    @PostMapping(DoctorConstants.CREATE_DOCTOR_END_POINT)
    public ResponseEntity<SuccessResponse<DoctorDto>> createDoctor(@Valid @RequestBody DoctorDto doctorDto) {
        DoctorDto createdDoctor = doctorService.saveDoctor(doctorDto);
        return new ResponseEntity<>(new SuccessResponse<>(true, createdDoctor), HttpStatus.OK);
    }

    /**
     * Retrieves a doctor by their UUID.
     *
     * @param doctorId    The ID of the doctor as a String, which will be converted to a UUID.
     * @return ResponseEntity containing a success response with the retrieved DoctorDto.
     */
    @GetMapping(DoctorConstants.DOCTOR_BY_ID_ENDPOINT)
    public ResponseEntity<?> getDoctorById(@PathVariable(DoctorConstants.PATH_VARIABLE_DOCTOR_ID) String doctorId) throws CustomException {
        DoctorDto doctorDto = doctorService.getDoctorById(doctorId);
        return new ResponseEntity<>(new SuccessResponse<>(true, doctorDto), HttpStatus.OK);
    }

    /**
     * Deletes a doctor by their ID.
     *
     * @param doctorId    The ID of the doctor to delete, obtained from the URL path.
     * @return ResponseEntity containing a SuccessResponse indicating the success status of the deletion operation.
     * @throws CustomException    If there's an issue with deleting the doctor.
     */
    @DeleteMapping(DoctorConstants.DOCTOR_BY_ID_ENDPOINT)
    public ResponseEntity<SuccessResponse<String>> deleteDoctorById(@PathVariable String doctorId) throws CustomException {
        return new ResponseEntity<>(new SuccessResponse<>(true, doctorService.deleteDoctorById(doctorId)), HttpStatus.OK);
    }
}
