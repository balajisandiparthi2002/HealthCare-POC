package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.constants.TestConstants;
import com.theelixrlabs.healthcare.exceptionHandler.PatientException;
import com.theelixrlabs.healthcare.model.DoctorPatientAssignmentModel;
import com.theelixrlabs.healthcare.repository.DoctorPatientAssignmentRepository;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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

    @Mock
    private DoctorPatientAssignmentRepository doctorPatientAssignmentRepository;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeletePatientById_Success() throws Exception {
        String patientId = TestConstants.PATIENT_ID;
        UUID validPatientId = UUID.fromString(patientId);
        when(validator.validateAndConvertToUUID(anyString(), anyString())).thenReturn(validPatientId);
        when(patientRepository.existsById(validPatientId)).thenReturn(true);
        when(doctorPatientAssignmentRepository.findByPatientIdAndDateOfUnassignmentNull(validPatientId)).thenReturn(Optional.empty());
        doNothing().when(patientRepository).deleteById(validPatientId);
        when(messageUtil.getMessage(anyString(), any(Object[].class))).thenReturn(TestConstants.PATIENT_DELETED_SUCCESSFULLY);
        String testResult = patientService.deletePatientById(patientId);
        String expectedResult = TestConstants.PATIENT_DELETED_SUCCESSFULLY;
        assertEquals(expectedResult, testResult);
        verify(patientRepository, times(1)).deleteById(validPatientId);
    }

    @Test
    public void testDeletePatientById_PatientAssignedToDoctor() throws Exception {
        String patientId = TestConstants.PATIENT_ID;
        UUID validPatientId = UUID.fromString(patientId);
        when(validator.validateAndConvertToUUID(anyString(), anyString())).thenReturn(validPatientId);
        when(patientRepository.existsById(validPatientId)).thenReturn(true);
        when(messageUtil.getMessage(PatientConstants.PATIENT_DELETION_FAILED_ASSIGNED_TO_DOCTOR)).thenReturn(TestConstants.DELETION_FAILED);
        when(doctorPatientAssignmentRepository.findByPatientIdAndDateOfUnassignmentNull(validPatientId)).thenReturn(Optional.of(new DoctorPatientAssignmentModel()));
        Exception exception = assertThrows(PatientException.class, () -> {
            patientService.deletePatientById(patientId);
        });
        String expectedResult = TestConstants.DELETION_FAILED;
        assertEquals(expectedResult, exception.getMessage());
    }
}
