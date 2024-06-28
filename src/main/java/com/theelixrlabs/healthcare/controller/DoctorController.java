package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.service.DoctorService;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        return new ResponseEntity<>(new SuccessResponse<>(true,createdDoctor),HttpStatus.OK);
    }
}
