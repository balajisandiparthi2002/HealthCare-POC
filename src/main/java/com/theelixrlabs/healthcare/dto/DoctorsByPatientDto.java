package com.theelixrlabs.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class DoctorsByPatientDto {
    private PatientDto patient;
    private List<DoctorDto> doctors;
}
