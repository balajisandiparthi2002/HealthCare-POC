package com.theelixrlabs.healthcare.dto;

import lombok.Data;
import java.util.List;

/**
 * Data transfer object (DTO) representing list of assigned doctors.
 * It is used for getting the data from aggregation pipeline.
 */
@Data
public class AssignedDoctorDto {
    private List<DoctorDto> assignedDoctors;
}
