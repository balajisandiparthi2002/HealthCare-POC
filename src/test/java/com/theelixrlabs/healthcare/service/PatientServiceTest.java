package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.constants.TestConstants;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.model.PatientModel;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Contains test methods for Patient Service
 * uses mock Repository to handle db requests
 */
@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private Validator validator;
    @Mock
    private MessageUtil messageUtil;
    @InjectMocks
    private PatientService patientService;
    private PatientModel patientModel;

    @BeforeEach
    void setPatientModel() {
        patientModel = PatientModel.builder()
                .id(UUID.randomUUID())
                .patientFirstName("Sambit")
                .patientLastName("Sahu")
                .patientAadhaarNumber("456783452698")
                .build();
    }

    /**
     * Test successful get request based on valid id
     * Checks for the service handles valid UUID and return patient dto
     *
     * @throws Exception if anything fails
     */
    @Test
    public void getPatientById_Success() throws Exception {
        String patientId = patientModel.getId().toString();
        UUID validPatientId = UUID.fromString(patientId);
        when(validator.validateAndConvertToUUID(patientId, PatientConstants.INVALID_UUID_KEY)).thenReturn(validPatientId);
        when(patientRepository.findById(validPatientId)).thenReturn(Optional.of(patientModel));
        PatientDto patientDto = patientService.getPatientById(patientId);
        assertNotNull(patientDto);
        assertEquals(patientId, patientDto.getId().toString());
        assertEquals(patientModel.getPatientFirstName(), patientDto.getPatientFirstName());
        assertEquals(patientModel.getPatientLastName(), patientDto.getPatientLastName());
        assertEquals(patientModel.getPatientAadhaarNumber(), patientDto.getPatientAadhaarNumber());
        Mockito.verify(patientRepository, Mockito.times(1)).findById(validPatientId);
    }

    /**
     * Test failure get request based on invalid id
     * Verifies for valid exception if patient not found
     *
     * @throws Exception if anything fails
     */
    @Test
    public void getPatientById_Failure() throws Exception {
        String patientId = UUID.randomUUID().toString();
        UUID validPatientId = UUID.fromString(patientId);
        when(validator.validateAndConvertToUUID(patientId, PatientConstants.INVALID_UUID_KEY)).thenReturn(validPatientId);
        when(patientRepository.findById(validPatientId)).thenReturn(Optional.empty());
        when(messageUtil.getMessage(PatientConstants.PATIENT_NOT_FOUND_KEY)).thenReturn(TestConstants.PATIENT_NOT_FOUND_MESSAGE);
        Exception actualExceptionResponse =
                assertThrows(Exception.class, () -> patientService.getPatientById(patientId));
        assertEquals(TestConstants.PATIENT_NOT_FOUND_MESSAGE, actualExceptionResponse.getMessage());
    }
}
