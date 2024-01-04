package com.demo.api.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.api.Country;
import com.demo.api.PostStudentResult;
import com.demo.api.State;
import com.demo.api.repository.StateRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;

@Service
public class StateService {
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private StateRepository StateRepo;
	
	private static final Logger logger = LoggerFactory.getLogger(StateService.class);
	
	
	public List<State> getStateData(Integer id, Integer status, String mode, Integer country, String stateName) {
		List<Tuple> tuples = (id != null || status != null|| mode != null || country != null || stateName != null) 
				? StateRepo.getState(id, status, mode, country,stateName) : StateRepo.getState(null,null,null,null,null);

	    return tuples.stream()
	            .map(tuple -> {
	                State stateResponseEntity = new State();
	                stateResponseEntity.setStateId(tuple.get("state_id", Integer.class));
	                stateResponseEntity.setStateCode(tuple.get("state_code", String.class));
	                stateResponseEntity.setStateName(tuple.get("state_name", String.class));
	                stateResponseEntity.setStateTin(tuple.get("state_tin", String.class));
	                stateResponseEntity.setCountryFromTuple(tuple);
	                stateResponseEntity.setStateStatusFromTuple(tuple);
	                

	                return stateResponseEntity;
	            })
	            .collect(Collectors.toList());
	}
	
	public List<State> getStateByCountryStateName(Integer country, String stateName) {
	    List<Tuple> tuples = (country != null && stateName != null) 
	            ? StateRepo.getStateByCountryandStateName(country, stateName)
	            : Collections.emptyList();

	    return tuples.stream()
	            .map(tuple -> {
	                State stateResponseEntity = new State();
	                stateResponseEntity.setStateId(tuple.get("state_id", Integer.class));
	                stateResponseEntity.setStateCode(tuple.get("state_code", String.class));
	                stateResponseEntity.setStateName(tuple.get("state_name", String.class));
	                stateResponseEntity.setStateTin(tuple.get("state_tin", String.class));
	                stateResponseEntity.setCountryFromTuple(tuple);
	                stateResponseEntity.setStateStatusFromTuple(tuple);

	                return stateResponseEntity;
	            })
	            .collect(Collectors.toList());
	}
	
	@Transactional
	public PostStudentResult putState(String lastUpdatedBy, String stateCode, String stateName, String stateTin, 
			Integer countryId, Integer stateStatus, Integer stateId) {
	    // Call the insert method
		StateRepo.updateState(lastUpdatedBy, stateCode, stateName, stateTin, countryId, stateStatus, stateId);

	    // Call the select method to get the response
		List<Object[]> result = StateRepo.getStateUpdateResult(stateId);

		if (!result.isEmpty()) {
	        Object[] row = result.get(0);
	        String outMessage = (String) row[0];
	        int outStatusCode = (int) row[1];

	        return new PostStudentResult(outMessage, outStatusCode);
	    }

	    // Handle empty result if needed
	    return new PostStudentResult("Unable To Update State", 400);
	}
	
	@Transactional
	public PostStudentResult putStateStatus(Integer stateStatus, Integer stateId) {
	    // Call the insert method
		StateRepo.updateStateStatus(stateStatus, stateId);

	    // Call the select method to get the response
		List<Object[]> result = StateRepo.getStateStatusResult(stateId);

		if (!result.isEmpty()) {
	        Object[] row = result.get(0);
	        String outMessage = (String) row[0];
	        int outStatusCode = (int) row[1];

	        return new PostStudentResult(outMessage, outStatusCode);
	    }

	    // Handle empty result if needed
	    return new PostStudentResult("Unable To Update State", 400);
	}
	
	
	public PostStudentResult callStateProcedure(String createdBy, String stateCode, String stateName, String stateTin,
			Integer countryId, Integer stateStatus) {
		try {
        // Create a StoredProcedureQuery
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("post_state");

        // Set input parameters
        storedProcedure.registerStoredProcedureParameter("createdBy", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("stateCode", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("stateName", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("stateTin", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("countryId", Integer.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("stateStatus", Integer.class, ParameterMode.IN);

        // Set output parameters
        storedProcedure.registerStoredProcedureParameter("out_message", String.class, ParameterMode.OUT);
        storedProcedure.registerStoredProcedureParameter("out_statusCode", Integer.class, ParameterMode.OUT);

        // Set parameter values
        storedProcedure.setParameter("createdBy", createdBy);
        storedProcedure.setParameter("stateCode", stateCode);
        storedProcedure.setParameter("stateName", stateName);
        storedProcedure.setParameter("stateTin", stateTin);
        storedProcedure.setParameter("countryId", countryId);
        storedProcedure.setParameter("stateStatus", stateStatus);

        // Execute the stored procedure
        storedProcedure.execute();

        // Get output parameter values
        String outMessage = (String) storedProcedure.getOutputParameterValue("out_message");
        Integer outStatusCode = (Integer) storedProcedure.getOutputParameterValue("out_statusCode");
        
//        System.out.println("outMessage: " + outMessage);
//        System.out.println("outStatusCode: " + outStatusCode);

        // Return the response object
        return new PostStudentResult(outMessage, outStatusCode);
		}catch (Exception e) {
	        // Log the exception
	        logger.error("Error executing stored procedure", e);

	        // Handle the exception, you might want to return an error response or re-throw the exception
	        throw new RuntimeException("Error executing stored procedure", e);
	    }
    }

}
