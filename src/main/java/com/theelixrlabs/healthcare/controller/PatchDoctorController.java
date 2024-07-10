package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import com.theelixrlabs.healthcare.service.PatchDoctorService;
import com.theelixrlabs.healthcare.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Controller layer for accepting user request and do patch operations.
 * Return responses along with updated doctorDto.
 */
@RestController
public class PatchDoctorController {

    private final PatchDoctorService patchDoctorService;

    private final Validator validator;

    public PatchDoctorController(PatchDoctorService patchDoctorService, Validator validator) {
        this.patchDoctorService = patchDoctorService;
        this.validator = validator;
    }

    /**
     * Controller method for handling incoming Patch request.
     *
     * @param doctorId  Doctor id as string.
     * @param doctorDto Data transfer object containing doctor information.
     * @return ResponseEntity containing success response alone with modified doctor details.
     */
    @PatchMapping(DoctorConstants.PATCH_DOCTOR_ENDPOINT)
    public ResponseEntity<SuccessResponse<DoctorDto>> patchDoctorById(@PathVariable String doctorId, @RequestBody DoctorDto doctorDto) {
        UUID uuid = validator.validateAndConvertToUUID(doctorId, MessageConstants.INVALID_UUID);
        DoctorDto updatedDoctorDto = patchDoctorService.patchDoctorById(uuid, doctorDto);
        return new ResponseEntity<>(new SuccessResponse<>(true, updatedDoctorDto, null), HttpStatus.OK);
    }
}
