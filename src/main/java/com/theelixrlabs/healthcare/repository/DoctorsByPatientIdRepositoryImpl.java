package com.theelixrlabs.healthcare.repository;

import com.theelixrlabs.healthcare.constants.AggregationConstant;
import com.theelixrlabs.healthcare.constants.DoctorPatientAssignmentConstants;
import com.theelixrlabs.healthcare.dto.AssignedDoctorDto;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of DoctorsByPatientIdRepository that retrieves assigned doctors for a patient using MongoDB aggregation.
 */
@Component
public class DoctorsByPatientIdRepositoryImpl implements DoctorsByPatientIdRepository {

    public MongoTemplate mongoTemplate;

    public DoctorsByPatientIdRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Retrieves a list of assigned doctors for a given patient ID using MongoDB aggregation.
     *
     * @param patientId    The unique identifier of the patient.
     * @return A list of AssignedDoctorDto objects representing assigned doctors.
     */
    @Override
    public List<AssignedDoctorDto> getDoctorsByPatientId(UUID patientId) {
        AggregationOperation addFieldsOperation = Aggregation.addFields()
                .addField(AggregationConstant.DOCTORS_DATE_OF_ASSIGNMENT).withValue(AggregationConstant.DATE_OF_ASSIGNMENT)
                .build();

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where(AggregationConstant.PATIENT_ID).is(patientId)
                                .and(AggregationConstant.DATE_OF_UNASSIGNMENT).isNull()
                ),
                Aggregation.lookup(
                        AggregationConstant.DOCTORS,
                        AggregationConstant.DOCTOR_ID,
                        AggregationConstant.ID,
                        AggregationConstant.DOCTORS
                ),
                Aggregation.unwind(AggregationConstant.DOCTORS),
                addFieldsOperation,
                Aggregation.group(AggregationConstant.PATIENT_ID)
                        .push(AggregationConstant.DOCTORS).as(AggregationConstant.ASSIGNED_DOCTORS),
                Aggregation.project()
                        .andExclude(AggregationConstant.ID)
                        .andInclude(AggregationConstant.ASSIGNED_DOCTORS)
        );
        AggregationResults<AssignedDoctorDto> aggregationResults = mongoTemplate.aggregate(
                aggregation,
                DoctorPatientAssignmentConstants.DB_COLLECTION_NAME,
                AssignedDoctorDto.class
        );
        return aggregationResults.getMappedResults();
    }
}
