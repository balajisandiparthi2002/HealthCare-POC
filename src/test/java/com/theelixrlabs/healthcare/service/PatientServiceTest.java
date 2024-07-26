package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.exceptionHandler.PatientException;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private MessageUtil messageUtil;

    @Mock
    private Validator validator;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeletePatientById_Success() throws Exception {
        String patientId = "641cb90a-9d64-468d-8e17-7efdd26482c3";
        UUID validPatientId = UUID.fromString(patientId);
        when(validator.validateAndConvertToUUID(anyString(), anyString())).thenReturn(validPatientId);
        when(patientRepository.existsById(validPatientId)).thenReturn(true);
        PatientService spyPatientService = spy(patientService);
        doReturn(false).when(spyPatientService).isPatientAssignedToDoctor(validPatientId);
        doNothing().when(patientRepository).deleteById(validPatientId);
        when(messageUtil.getMessage(anyString(), any(Object[].class))).thenReturn("Patient deleted successfully");
        String testResult = spyPatientService.deletePatientById(patientId);
        assertEquals("Patient deleted successfully", testResult);
        verify(patientRepository, times(1)).deleteById(validPatientId);
    }

    @Test
    public void testDeletePatientById_PatientAssignedToDoctor() throws Exception {
        String patientId = "641cb90a-9d64-468d-8e17-7efdd26482c3";
        UUID validPatientId = UUID.fromString(patientId);
        when(validator.validateAndConvertToUUID(patientId, anyString())).thenReturn(validPatientId);
        when(patientRepository.existsById(validPatientId)).thenReturn(true);
        when(messageUtil.getMessage(PatientConstants.PATIENT_DELETION_FAILED_ASSIGNED_TO_DOCTOR)).thenReturn("Deletion Failed. Patient is currently assigned to a Doctor.");
        PatientService spyPatientService = spy(patientService);
        doReturn(true).when(spyPatientService).isPatientAssignedToDoctor(validPatientId);
        Exception exception = assertThrows(PatientException.class, () -> {
            spyPatientService.deletePatientById(patientId);
        });
        assertEquals("Deletion Failed. Patient is currently assigned to a Doctor.", exception.getMessage());
    }
}
