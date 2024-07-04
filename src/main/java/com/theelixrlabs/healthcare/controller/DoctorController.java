package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.service.DoctorService;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

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
     * @param id The ID of the doctor as a String, which will be converted to a UUID.
     * @return ResponseEntity containing a success response with the retrieved DoctorDto.
     */
    @GetMapping(DoctorConstants.GET_DOCTOR_BY_ID_ENDPOINT)
    public ResponseEntity<?> getDoctorById(@PathVariable(DoctorConstants.PATH_VARIABLE_DOCTOR_ID) String id) {
        UUID doctorId;
        try {
            doctorId = UUID.fromString(id);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new CustomException(MessageConstants.INVALID_UUID);
        }
        DoctorDto doctorDto = doctorService.getDoctorById(doctorId);
        return new ResponseEntity<>(new SuccessResponse<>(true, doctorDto), HttpStatus.OK);
    }
}
