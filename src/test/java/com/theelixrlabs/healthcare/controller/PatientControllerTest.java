package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.ApiPathsConstant;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.exceptionHandler.PatientNotFoundException;
import com.theelixrlabs.healthcare.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Contains test methods for Patient Controller
 * uses mock mvc to simulate Http requests and verify the behaviour of each method
 */
@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PatientService patientService;
    private PatientDto patientDto;

    @BeforeEach
    void setPatientDto() {
        patientDto = PatientDto.builder()
                .id(UUID.randomUUID())
                .patientFirstName("Sambit")
                .patientLastName("Sahu")
                .patientAadhaarNumber("456783452698")
                .build();
    }

    /**
     * It tests a successful get request based on the Valid UUID
     * Mocks the service to return a PatientDto
     *
     * @throws Exception if assertion or request fails
     */
    @Test
    public void getPatientByIdSuccessTest() throws Exception {
        String patientId = patientDto.getId().toString();
        Mockito.when(patientService.getPatientById(patientId)).thenReturn(patientDto);
        ResultActions response = mockMvc.perform(get(ApiPathsConstant.PATIENT_BY_ID_ENDPOINT, patientId)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseData.id").value(patientDto.getId().toString()))
                .andExpect(jsonPath("$.responseData.patientFirstName").value(patientDto.getPatientFirstName()))
                .andExpect(jsonPath("$.responseData.patientLastName").value(patientDto.getPatientLastName()))
                .andExpect(jsonPath("$.responseData.patientAadhaarNumber").value(patientDto.getPatientAadhaarNumber()));
    }

    /**
     * It tests the handling of exception based on the Invalid UUID
     * Mocks the service to return an exception
     *
     * @throws Exception if assertion or request fails
     */
    @Test
    public void getPatientByIdFailureTest() throws Exception {
        String patientId = UUID.randomUUID().toString();
        Mockito.when(patientService.getPatientById(patientId)).thenThrow(new PatientNotFoundException("Patient not found"));
        ResultActions response = mockMvc.perform(get(ApiPathsConstant.PATIENT_BY_ID_ENDPOINT, patientId)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors").value("Patient not found"));
    }
}
