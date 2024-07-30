package com.theelixrlabs.healthcare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theelixrlabs.healthcare.constants.ApiPathsConstant;
import com.theelixrlabs.healthcare.constants.TestConstants;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.exceptionHandler.PatientNotFoundException;
import com.theelixrlabs.healthcare.response.FailureResponse;
import com.theelixrlabs.healthcare.response.SuccessResponse;
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

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Contains test methods for Patient Controller
 * uses mock mvc to simulate Http requests and verify the behaviour of each method
 */
@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
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
    public void getPatientById_Success() throws Exception {
        String patientId = patientDto.getId().toString();
        Mockito.when(patientService.getPatientById(patientId)).thenReturn(patientDto);
        ResultActions actualResponse = mockMvc.perform(get(ApiPathsConstant.PATIENT_BY_ID_ENDPOINT, patientId)
                .contentType(MediaType.APPLICATION_JSON));
        actualResponse.andExpect(status().isOk());
        String actualResponseData = actualResponse.andReturn().getResponse().getContentAsString();
        SuccessResponse<PatientDto> expectedSuccessResponse = new SuccessResponse<>(true,patientDto,null);
        String expectedResponseData = objectMapper.writeValueAsString(expectedSuccessResponse);
        assertEquals(expectedResponseData,actualResponseData);
    }

    /**
     * It tests the handling of exception based on the Invalid UUID
     * Mocks the service to return an exception
     *
     * @throws Exception if assertion or request fails
     */
    @Test
    public void getPatientById_Failure() throws Exception {
        String patientId = UUID.randomUUID().toString();
        Mockito.when(patientService.getPatientById(patientId)).thenThrow(new PatientNotFoundException(TestConstants.PATIENT_NOT_FOUND_MESSAGE));
        ResultActions response = mockMvc.perform(get(ApiPathsConstant.PATIENT_BY_ID_ENDPOINT, patientId)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isNotFound());
        String actualResponseData = response.andReturn().getResponse().getContentAsString();
        FailureResponse failureResponse = new FailureResponse(false, Arrays.asList(TestConstants.PATIENT_NOT_FOUND_MESSAGE));
        String expectedResponseData = objectMapper.writeValueAsString(failureResponse);
        assertEquals(expectedResponseData,actualResponseData);
    }
}
