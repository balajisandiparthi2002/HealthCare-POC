package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.exceptionHandler.PatientNotFoundException;
import com.theelixrlabs.healthcare.model.PatientModel;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

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
    public void getPatientByIdSuccessTest() throws Exception {
        String patientId = patientModel.getId().toString();
        UUID validPatientId = UUID.fromString(patientId);
        Mockito.when(validator.validateAndConvertToUUID(patientId, PatientConstants.INVALID_UUID_KEY)).thenReturn(validPatientId);
        Mockito.when(patientRepository.findById(validPatientId)).thenReturn(Optional.of(patientModel));
        PatientDto patientDto = patientService.getPatientById(patientId);
        Assertions.assertNotNull(patientDto);
        Assertions.assertEquals(patientId, patientDto.getId().toString());
        Assertions.assertEquals(patientModel.getPatientFirstName(), patientDto.getPatientFirstName());
        Assertions.assertEquals(patientModel.getPatientLastName(), patientDto.getPatientLastName());
        Assertions.assertEquals(patientModel.getPatientAadhaarNumber(), patientDto.getPatientAadhaarNumber());
        Mockito.verify(patientRepository, Mockito.times(1)).findById(validPatientId);
    }

    /**
     * Test failure get request based on invalid id
     * Verifies for valid exception if patient not found
     *
     * @throws Exception if anything fails
     */
    @Test
    public void getPatientByIdFailureTest() throws Exception {
        String patientId = UUID.randomUUID().toString();
        UUID validPatientId = UUID.fromString(patientId);
        Mockito.when(validator.validateAndConvertToUUID(patientId, PatientConstants.INVALID_UUID_KEY)).thenReturn(validPatientId);
        Mockito.when(patientRepository.findById(validPatientId)).thenReturn(Optional.empty());
        Mockito.when(messageUtil.getMessage(PatientConstants.PATIENT_NOT_FOUND_KEY)).thenReturn("Patient not found");
        PatientNotFoundException patientNotFoundException = Assertions.assertThrows(PatientNotFoundException.class, () -> {
            patientService.getPatientById(patientId);
        });
        Assertions.assertEquals("Patient not found", patientNotFoundException.getMessage());
    }
}
