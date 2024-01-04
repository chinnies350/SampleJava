package com.demo.api.service;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.mapping.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.api.City;
import com.demo.api.Country;
import com.demo.api.ListTypeValues;
import com.demo.api.PostStudentResult;
import com.demo.api.State;
import com.demo.api.message.CityResponseEntity;
import com.demo.api.repository.CityRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;

@Service
public class CityService {
	
	
	
	@Autowired
	private CityRepository CityRepo;
	
	@Autowired
	private EntityManager entityManager;
	
	private static final Logger logger = LoggerFactory.getLogger(CityService.class);
	
	public List<City> getCityData(Integer id, Integer status, String mode, String cityName, Integer state) {
		List<Tuple> tuples = (id != null || status != null || mode != null || cityName !=null || state != null) 
				? CityRepo.getCity(id, status, mode, cityName, state) : CityRepo.getCity(null, null,null,null,null);
//	    List<Tuple> tuples;
//	    if (id != null && status != null) {
//	        tuples = CityRepo.getCity(id, status);
//	    } else {
//	        tuples = CityRepo.getCity(null, null); // Assuming this method fetches data without parameters
//	    }

	    return tuples.stream()
	            .map(tuple -> {
	                City cityResponseEntity = new City();
	                cityResponseEntity.setCityId(tuple.get("city_id", Integer.class));
	                cityResponseEntity.setCityName(tuple.get("city_name", String.class));
	                cityResponseEntity.setCityStatusFromTuple(tuple);
	                cityResponseEntity.setCountryFromTuple(tuple);
	                cityResponseEntity.setStateFromTuple(tuple);
	                cityResponseEntity.setCreatedBy(tuple.get("created_by", String.class));
	                cityResponseEntity.setLastUpdatedBy(tuple.get("last_updated_by", String.class));
	                java.sql.Timestamp timestamp = tuple.get("creation_date", java.sql.Timestamp.class);
	                LocalDateTime localDateTime = timestamp.toLocalDateTime();
	                cityResponseEntity.setCreateDateTime(localDateTime);
	                java.sql.Timestamp timestampUpdate = tuple.get("last_updated_date", java.sql.Timestamp.class);
		             if (timestampUpdate != null) {
		                 LocalDateTime localDateTimeUpdate = timestampUpdate.toLocalDateTime();
		                 cityResponseEntity.setUpdateDateTime(localDateTimeUpdate);
		             } else {
		                 // Handle the case where timestampUpdate is null, e.g., set a default value or log a message
		                 cityResponseEntity.setUpdateDateTime(null); // or set to a default LocalDateTime value
		             }

//		             System.out.println("cityResponseEntity"+cityResponseEntity);
		             return cityResponseEntity;
	                
	            })
	            .collect(Collectors.toList());
	}
	
	
	
//	public List<CityResponseEntity> getCity(Integer id, Integer state) {
//	    if (id == null && state == null) {
//	        // If all parameters are null or 0, return all cities
//	    	return CityRepo.getCity(null, null);
//	    } else {
//	        // Otherwise, apply the filtering logic
//	        return CityRepo.getCity(id, state);
//	    }
//	}
	

	@Transactional
	public PostStudentResult postCity(String createdBy, String cityName, Integer cityStatus, Integer country, Integer state) {
	    // Call the insert method
	    CityRepo.insertCity(createdBy, cityName, cityStatus, country, state);

	    // Call the select method to get the response
	    List<Object[]> result = CityRepo.getCityResponse(cityName);

	    // Assuming the first row of the result is the relevant data
	    if (!result.isEmpty()) {
	        Object[] row = result.get(0);
	        String outMessage = (String) row[0];
	        int outStatusCode = (int) row[1];

	        return new PostStudentResult(outMessage, outStatusCode);
	    }

	    // Handle empty result if needed
	    return new PostStudentResult("Unable To Create City", 400);
	}
	
	@Transactional
	public PostStudentResult putCity(String lastUpdatedBy, String cityName, Integer cityStatus, Integer country, Integer state, Integer cityId) {
	    // Call the insert method
	    CityRepo.updateCity(lastUpdatedBy, cityName, cityStatus, country, state, cityId);

	    // Call the select method to get the response
	    List<Object[]> result = CityRepo.getCityUpdateResult(cityId);

	    // Assuming the first row of the result is the relevant data
	    if (!result.isEmpty()) {
	        Object[] row = result.get(0);
	        String outMessage = (String) row[0];
	        int outStatusCode = (int) row[1];

	        return new PostStudentResult(outMessage, outStatusCode);
	    }

	    // Handle empty result if needed
	    return new PostStudentResult("Unable To Update City", 400);
	}
	
	@Transactional
	public PostStudentResult putCityStatus( Integer cityStatus, Integer cityId) {
	    // Call the insert method
	    CityRepo.updateCityStatus( cityStatus, cityId);

	    // Call the select method to get the response
	    List<Object[]> result = CityRepo.getCityStatusResult(cityId);

	    // Assuming the first row of the result is the relevant data
	    if (!result.isEmpty()) {
	        Object[] row = result.get(0);
	        String outMessage = (String) row[0];
	        int outStatusCode = (int) row[1];

	        return new PostStudentResult(outMessage, outStatusCode);
	    }

	    // Handle empty result if needed
	    return new PostStudentResult("Unable To Update City", 400);
	}
	
	@Transactional
	public PostStudentResult putCityListTypeName( String listTypeValueName, Integer cityId) {
	    // Call the insert method
	    CityRepo.updateCitylistTypeValueName( listTypeValueName, cityId);

	    // Call the select method to get the response
	    List<Object[]> result = CityRepo.getCitylistTypeNameResult(cityId);

	    // Assuming the first row of the result is the relevant data
	    if (!result.isEmpty()) {
	        Object[] row = result.get(0);
	        String outMessage = (String) row[0];
	        int outStatusCode = (int) row[1];

	        return new PostStudentResult(outMessage, outStatusCode);
	    }

	    // Handle empty result if needed
	    return new PostStudentResult("Unable To Update City", 400);
	}

	
//	public PostStudentResult callCityProcedure(String createdBy, String cityName, Integer cityStatus,
//			Integer countryId, Integer stateId) {
//		try {
//        // Create a StoredProcedureQuery
//        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("post_city");
//
//        // Set input parameters
//        storedProcedure.registerStoredProcedureParameter("createdBy", String.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("cityName", String.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("cityStatus", Integer.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("countryId", Integer.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("stateId", Integer.class, ParameterMode.IN);
//
//        // Set output parameters
//        storedProcedure.registerStoredProcedureParameter("out_message", String.class, ParameterMode.OUT);
//        storedProcedure.registerStoredProcedureParameter("out_statusCode", Integer.class, ParameterMode.OUT);
//
//        // Set parameter values
//        storedProcedure.setParameter("createdBy", createdBy);
//        storedProcedure.setParameter("cityName", cityName);
//        storedProcedure.setParameter("cityStatus", cityStatus);
//        storedProcedure.setParameter("countryId", countryId);
//        storedProcedure.setParameter("stateId", stateId);
//
//        // Execute the stored procedure
//        storedProcedure.execute();
//
//        // Get output parameter values
//        String outMessage = (String) storedProcedure.getOutputParameterValue("out_message");
//        Integer outStatusCode = (Integer) storedProcedure.getOutputParameterValue("out_statusCode");
//        
////        System.out.println("outMessage: " + outMessage);
////        System.out.println("outStatusCode: " + outStatusCode);
//
//        // Return the response object
//        return new PostStudentResult(outMessage, outStatusCode);
//		}catch (Exception e) {
//	        // Log the exception
//	        logger.error("Error executing stored procedure", e);
//
//	        // Handle the exception, you might want to return an error response or re-throw the exception
//	        throw new RuntimeException("Error executing stored procedure", e);
//	    }
//    }

}
