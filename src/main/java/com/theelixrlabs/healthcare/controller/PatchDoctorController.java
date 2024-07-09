package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.exceptionHandler.CustomException;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import com.theelixrlabs.healthcare.service.PatchDoctorService;
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

    public PatchDoctorController(PatchDoctorService patchDoctorService) {
        this.patchDoctorService = patchDoctorService;
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
        UUID uuid=patchDoctorService.convertUuidToString(doctorId);
        DoctorDto newDoctorDto = patchDoctorService.patchDoctorById(uuid, doctorDto);
        return new ResponseEntity<>(new SuccessResponse<>(true, newDoctorDto), HttpStatus.OK);
    }
}
