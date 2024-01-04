package com.demo.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.demo.api.City;
import com.demo.api.Country;
import com.demo.api.ListTypeValues;
import com.demo.api.PostStudentResult;
import com.demo.api.controller.CountryController;
import com.demo.api.repository.CountryRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;

@Service
public class CountryService {
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private CountryRepository ConRepo;
	
	private static final Logger logger = LoggerFactory.getLogger(CountryService.class);
	
	public List<Country> getCountryData(Integer id, Integer status, String mode, String countryName) {
		List<Tuple> tuples = (id != null || status != null || mode != null || countryName !=null) 
				? ConRepo.getCountry(id,status,mode,countryName) : ConRepo.getCountry(null,null,null,null);

	    return tuples.stream()
	            .map(tuple -> {
	                Country countryResponseEntity = new Country();
	                countryResponseEntity.setCountryId(tuple.get("country_id", Integer.class));
	                countryResponseEntity.setCountryName(tuple.get("country_name", String.class));
	                countryResponseEntity.setCountryCode(tuple.get("country_code", String.class));
	                countryResponseEntity.setCurrencySymbol(tuple.get("currency_symbol",String.class));
	                countryResponseEntity.setCurrencyCode(tuple.get("currency_code", String.class));
	                countryResponseEntity.setMaxPhoneNumberLength(tuple.get("max_phonenumber_length",String.class));
	                countryResponseEntity.setDefaultCountry(tuple.get("default_country", Boolean.class) != null
	                        ? tuple.get("default_country", Boolean.class)
	                        : Boolean.FALSE);
	                countryResponseEntity.setCountryStatusFromTuple(tuple);
	                

	                return countryResponseEntity;
	            })
	            .collect(Collectors.toList());
	}
	
	@Transactional
	public PostStudentResult postCountry(String createdBy, String countryCode, String countryName, String currencySymbol, String currencyCode, 
			Boolean defaultCountry, String maxPhonenumberLength, Integer countryStatus) {
	    // Call the insert method
		ConRepo.insertCountry(createdBy, countryCode, countryName, currencySymbol, currencyCode, defaultCountry, maxPhonenumberLength, countryStatus);

	    // Call the select method to get the response
		List<Object[]> result = ConRepo.getCountryInsertResult(countryCode, countryName);

		if (!result.isEmpty()) {
	        Object[] row = result.get(0);
	        String outMessage = (String) row[0];
	        int outStatusCode = (int) row[1];

	        return new PostStudentResult(outMessage, outStatusCode);
	    }

	    // Handle empty result if needed
	    return new PostStudentResult("Unable To Create Country", 400);
	}
	
	@Transactional
	public PostStudentResult putCountry(String lastUpdatedBy, String countryCode, String countryName, String currencyCode, 
			Boolean defaultCountry, String maxPhonenumberLength, String currencySymbol, Integer countryStatus, Integer countryId) {
	    // Call the insert method
		ConRepo.updateCountry(lastUpdatedBy, countryCode, countryName, currencyCode, defaultCountry, maxPhonenumberLength, currencySymbol, countryStatus, countryId);

	    // Call the select method to get the response
		List<Object[]> result = ConRepo.getCountryUpdateResult(countryId);

		if (!result.isEmpty()) {
	        Object[] row = result.get(0);
	        String outMessage = (String) row[0];
	        int outStatusCode = (int) row[1];

	        return new PostStudentResult(outMessage, outStatusCode);
	    }

	    // Handle empty result if needed
	    return new PostStudentResult("Unable To Update Country", 400);
	}
	
	@Transactional
	public PostStudentResult putCountryStatus(Integer countryStatus, Integer countryId) {
	    // Call the insert method
		ConRepo.updateCountryStatus(countryStatus, countryId);

	    // Call the select method to get the response
		List<Object[]> result = ConRepo.getCountryStatusResult(countryId);

		if (!result.isEmpty()) {
	        Object[] row = result.get(0);
	        String outMessage = (String) row[0];
	        int outStatusCode = (int) row[1];

	        return new PostStudentResult(outMessage, outStatusCode);
	    }

	    // Handle empty result if needed
	    return new PostStudentResult("Unable To Update CountryStatus", 400);
	}
	
	// Multiple Record Insert
	@Transactional
	    public List<PostStudentResult> insertCountries(List<Country> countries) {
	        List<PostStudentResult> results = new ArrayList<>();

	        for (Country country : countries) {
	            try {
	                // Check if country with the same code or name already exists
	                if (ConRepo.existsByCountryCodeOrCountryName(country.getCountryCode(), country.getCountryName())) {
	                    results.add(new PostStudentResult("Country Code or Name Already Exists", 409));
	                } else {
	                    // Validate or perform any necessary checks before saving
	                    // You can add custom validation logic here

	                	ConRepo.save(country);
	                    results.add(new PostStudentResult("Country Created Successfully", 200));
	                }
	            } catch (Exception e) {
	                // Handle other exceptions
	                results.add(new PostStudentResult("Unable To Create Country", 400));
	            }
	        }

	        return results;
	    }
	
	// Multiple Table insert
	@Transactional
	public PostStudentResult postMultiTable(String createdBy, String countryCode, String countryName, String currencySymbol,
	        String currencyCode, Boolean defaultCountry, String maxPhonenumberLength, Integer countryStatus, String stateCode,
	        String stateName, String stateTin, Integer stateStatus) {
	    
	    // Call the insert method
	    Integer insertedCountryId = ConRepo.insertCountryAndState(createdBy, countryCode, countryName, currencySymbol, currencyCode, 
	            defaultCountry, maxPhonenumberLength, countryStatus, stateCode, stateName, stateTin, stateStatus);

	    // Call the select method to get the response
	    List<Object[]> result = ConRepo.checkInsertStatus(insertedCountryId);

	    if (!result.isEmpty()) {
	        Object[] row = result.get(0);
	        String outMessage = (String) row[0];
	        int outStatusCode = ((Number) row[1]).intValue();

	        return new PostStudentResult(outMessage, outStatusCode);
	    }

	    // Handle empty result if needed
	    return new PostStudentResult("Unable To Update CountryStatus", 400);
	}
	
//	@Transactional
//	public PostStudentResult postMultCountry(List<String> createdBy, List<String> countryCode, List<String> countryName, List<String> currencySymbol, List<String> currencyCode, 
//			List<Boolean> defaultCountry, List<String> maxPhonenumberLength, List<Integer> countryStatus) {
//	    // Call the insert method
//		ConRepo.MultiinsertCountry(createdBy, countryCode, countryName, currencySymbol, currencyCode, defaultCountry, maxPhonenumberLength, countryStatus);
//
//	    // Call the select method to get the response
//		List<Object[]> result = ConRepo.getMultiCountryInsertResult(countryCode, countryName);
//
//		if (!result.isEmpty()) {
//	        Object[] row = result.get(0);
//	        String outMessage = (String) row[0];
//	        int outStatusCode = (int) row[1];
//
//	        return new PostStudentResult(outMessage, outStatusCode);
//	    }
//
//	    // Handle empty result if needed
//	    return new PostStudentResult("Unable To Create Country", 400);
//	}
	
//	public List<Country> insertMultCountries(List<Country> countries) {
//        // Save multiple countries
//        return ConRepo.saveAll(countries);
//    }
	
//	@Transactional
//	public void insertMultipleCountries(List<Country> countries) {
//		List<String> createdByList = countries.stream().map(Country::getCreatedBy).collect(Collectors.toList());
//	    List<String> countryCodesList = countries.stream().map(Country::getCountryCode).collect(Collectors.toList());
//	    List<String> countryNamesList = countries.stream().map(Country::getCountryName).collect(Collectors.toList());
//	    List<String> currencySymbolList = countries.stream().map(Country::getCurrencySymbol).collect(Collectors.toList());
//	    List<String> currencyCodeList = countries.stream().map(Country::getCurrencyCode).collect(Collectors.toList());
//	    List<Boolean> defaultCountryList = countries.stream().map(Country::isDefaultCountry).collect(Collectors.toList());
//	    List<String> maxPhoneNumberLengthList = countries.stream().map(Country::getMaxPhoneNumberLength).collect(Collectors.toList());
//	    List<ListTypeValues> countryStatusList = countries.stream().map(Country::getCountryStatus).collect(Collectors.toList());
//
//	    ConRepo.insertMultipleCountries(
//	            createdByList, countryCodesList, countryNamesList, currencySymbolList, currencyCodeList,
//	            defaultCountryList, maxPhoneNumberLengthList, countryStatusList
//	    );
//	}
//	
	
//	@Transactional
//    public void insertMultCountries(String createdBy, String countryCode, String countryName, List<Country> countries) {
//		ConRepo.insertMultCountries(createdBy, countryCode, countryName, countries);
//    }
//
//    public List<Object[]> getMultCountryInsertResult(String countryCode, String countryName) {
//        return ConRepo.getMultCountryInsertResult(countryCode, countryName);
//    }
//	public PostStudentResult callPostCountryProcedure(String createdBy, String countryCode, String countryName, String currencySymbol,
//			String currencyCode, Boolean defaultCountry, String maxPhoneNumberLength, Integer countryStatus) {
//		try {
//        // Create a StoredProcedureQuery
//        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("post_country");
//
//        // Set input parameters
//        storedProcedure.registerStoredProcedureParameter("createdBy", String.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("countryCode", String.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("countryName", String.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("currencySymbol", String.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("currencyCode", String.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("defaultCountry", Boolean.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("maxPhoneNumberLength", String.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("countryStatus", Integer.class, ParameterMode.IN);
//
//        // Set output parameters
//        storedProcedure.registerStoredProcedureParameter("out_message", String.class, ParameterMode.OUT);
//        storedProcedure.registerStoredProcedureParameter("out_statusCode", Integer.class, ParameterMode.OUT);
//
//        // Set parameter values
//        storedProcedure.setParameter("createdBy", createdBy);
//        storedProcedure.setParameter("countryCode", countryCode);
//        storedProcedure.setParameter("countryName", countryName);
//        storedProcedure.setParameter("currencySymbol", currencySymbol);
//        storedProcedure.setParameter("currencyCode", currencyCode);
//        storedProcedure.setParameter("defaultCountry", defaultCountry);
//        storedProcedure.setParameter("maxPhoneNumberLength", maxPhoneNumberLength);
//        storedProcedure.setParameter("countryStatus", countryStatus);
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
