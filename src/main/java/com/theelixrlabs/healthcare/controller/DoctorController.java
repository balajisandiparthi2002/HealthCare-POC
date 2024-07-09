package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.service.DoctorService;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import com.theelixrlabs.healthcare.validation.DoctorModelValidator;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;
import java.util.List;

/**
 * Rest Controller for handling HTTP request and response.
 */
@RestController
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorModelValidator doctorModelValidator;
    private final MessageUtil messageUtil;

    public DoctorController(DoctorService doctorService, DoctorModelValidator doctorModelValidator, MessageUtil messageUtil) {
        this.doctorService = doctorService;
        this.doctorModelValidator = doctorModelValidator;
        this.messageUtil = messageUtil;
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
            throw new CustomException(messageUtil.getMessage(MessageConstants.INVALID_UUID));
        }
        DoctorDto doctorDto = doctorService.getDoctorById(doctorId);
        return new ResponseEntity<>(new SuccessResponse<>(true, doctorDto), HttpStatus.OK);
    }

    /**
     * Retrieves a list of doctors by their name.
     *
     * @param doctorName the name of the doctor to search for
     * @return a ResponseEntity containing a SuccessResponse with a list of matching DoctorDto objects
     */
    @GetMapping(DoctorConstants.GET_DOCTORS_BY_NAME_ENDPOINT)
    public ResponseEntity<SuccessResponse<List<DoctorDto>>> getDoctorsByName
    (@RequestParam(DoctorConstants.DOCTOR_NAME_PARAM) String doctorName) {
        List<DoctorDto> doctorDtoList = doctorService.getDoctorsByName(doctorName);
        return new ResponseEntity<>(new SuccessResponse<>(true, doctorDtoList), HttpStatus.OK);
    }
}
