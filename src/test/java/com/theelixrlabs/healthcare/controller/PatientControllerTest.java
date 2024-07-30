package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.TestConstants;
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
        String patientId = TestConstants.PATIENT_ID;
        when(patientService.deletePatientById(patientId)).thenReturn(TestConstants.PATIENT_DELETED_SUCCESSFULLY);
        String expectedResult = TestConstants.PATIENT_DELETED_SUCCESSFULLY;
        mockMvc.perform(delete(TestConstants.PATIENT_BY_ID, patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TestConstants.SUCCESS_EXPRESSION).value(true))
                .andExpect(jsonPath(TestConstants.RESPONSE_MESSAGE_EXPRESSION).value(expectedResult));
    }

    @Test
    public void deletePatientById_Failure() throws Exception {
        String patientId = TestConstants.PATIENT_ID;
        when(patientService.deletePatientById(patientId)).thenThrow(new PatientNotFoundException(TestConstants.PATIENT_NOT_FOUND));
        String expectedResult = TestConstants.PATIENT_NOT_FOUND;
        mockMvc.perform(delete(TestConstants.PATIENT_BY_ID, patientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(TestConstants.SUCCESS_EXPRESSION).value(false))
                .andExpect(jsonPath(TestConstants.ERRORS_ARRAY_EXPRESSION).isArray())
                .andExpect(jsonPath(TestConstants.TEST_RESULT_ARRAY_EXPRESSION).value(expectedResult));
    }
}
