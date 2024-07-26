package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.exceptionHandler.PatientNotFoundException;
import com.theelixrlabs.healthcare.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Test
    public void deletePatientById_Success() throws Exception {
        String patientId = "641cb90a-9d64-468d-8e17-7efdd26482c3";
        when(patientService.deletePatientById(patientId)).thenReturn("Patient deleted successfully.");
        mockMvc.perform(delete("/patient/{patientId}", patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseMessage").value("Patient deleted successfully."));
    }

    @Test
    public void deletePatientById_Failure() throws Exception {
        String patientId = "641cb90a-9d64-468d-8e17-7efdd26482c3";
        when(patientService.deletePatientById(patientId)).thenThrow(new PatientNotFoundException("Patient not found."));
        mockMvc.perform(delete("/patient/{patientId}", patientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("Patient not found."));
    }
}
