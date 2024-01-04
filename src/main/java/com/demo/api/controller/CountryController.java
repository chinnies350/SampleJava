package com.demo.api.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.api.City;
import com.demo.api.Country;
import com.demo.api.MultiTableRequest;
import com.demo.api.ListTypeValues;
import com.demo.api.PostStudentResult;
import com.demo.api.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/email")
public class CountryController {
	
	@Autowired
	private CountryService Conservice;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(CountryController.class);
	
	
	@GetMapping("/country")
	public ResponseEntity<?> getCountry() {

	    try {
	        List<Country> countryList = Conservice.getCountryData(null,null,null,null);
	        if (!countryList.isEmpty()) {
	            return ResponseEntity.ok(countryList);
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message", "No Data");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	        }
	    } catch (Exception e) {
	        // Log the exception for debugging purposes
	        logger.error("An error occurred while processing the request", e);

	        // Provide a more meaningful response to the client
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
	    }
	}
	
	@GetMapping("/country/{id}")
	public ResponseEntity<?> getCountryById(@PathVariable Integer id) {

	    try {
	        List<Country> countryList = Conservice.getCountryData(id,null,null,null);
	        if (!countryList.isEmpty()) {
	            return ResponseEntity.ok(countryList);
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message", "Country With Id:" + id + " Not Found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	        }
	    } catch (Exception e) {
	        // Log the exception for debugging purposes
	        logger.error("An error occurred while processing the request", e);

	        // Provide a more meaningful response to the client
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
	    }
	}
	
	@GetMapping("/country/country_status/{status}")
	public ResponseEntity<?> getCountryByStatus(@PathVariable Integer status) {

	    try {
	        List<Country> countryList = Conservice.getCountryData(null,status,null,null);
	        if (!countryList.isEmpty()) {
	            return ResponseEntity.ok(countryList);
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message", "Country With status:" + status + " Not Found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	        }
	    } catch (Exception e) {
	        // Log the exception for debugging purposes
	        logger.error("An error occurred while processing the request", e);

	        // Provide a more meaningful response to the client
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
	    }
	}
	
	@GetMapping("/country/active_country")
	public ResponseEntity<?> getCountryByactiveciy() {
	    try {
	    	String mode  = "Active";
	        List<Country> countryList = Conservice.getCountryData(null, null, mode,null);
	
	        if (!countryList.isEmpty()) {
	            return ResponseEntity.ok(countryList);
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message"," Not Found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	        }
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@GetMapping("/country/countryname/{countryName}")
	public ResponseEntity<?> getCountryBycountryName(@PathVariable ("countryName") String countryName) {
	    try {
	        List<Country> countryList = Conservice.getCountryData(null, null, null,countryName);
	
	        if (!countryList.isEmpty()) {
	            return ResponseEntity.ok(countryList);
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message","Country With countryName: " +countryName +" Not Found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	        }
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	
	@PostMapping("/country")
	public ResponseEntity<PostStudentResult> postCountry(@RequestBody Country country) {
	    try {
	    	String createdBy = country.getCreatedBy();
	    	String countryCode = country.getCountryCode();
	    	String countryName = country.getCountryName();
	    	String currencySymbol = country.getCurrencySymbol();
	    	String currencyCode = country.getCurrencyCode();
	    	Boolean defaultCountry = country.isDefaultCountry();
	    	String maxPhoneNumberLength =country.getMaxPhoneNumberLength();
	    	ListTypeValues countryStatus = country.getCountryStatus();
	    	
	    	// Assuming ListTypeValues has a method to get an Integer value
	    	Integer countryStatusValue = (countryStatus != null) ? countryStatus.getListTypeValueId() : null;
	    	
	        PostStudentResult result = Conservice.postCountry(createdBy, countryCode, countryName,currencySymbol,
	        		currencyCode, defaultCountry,maxPhoneNumberLength,countryStatusValue);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	
	@PutMapping("/country/{countryid}")
	public ResponseEntity<PostStudentResult> putCountry(@RequestBody Country country, @PathVariable Integer countryid ) {
	    try {
	    	String lastUpdatedBy = country.getLastUpdatedBy();
	    	String countryCode = country.getCountryCode();
	    	String countryName = country.getCountryName();
	    	String currencyCode = country.getCurrencyCode();
	    	Boolean defaultCountry = country.isDefaultCountry();
	    	String maxPhoneNumberLength =country.getMaxPhoneNumberLength();
	    	String currencySymbol = country.getCurrencySymbol();
	    	ListTypeValues countryStatus = country.getCountryStatus();
	    	Integer countryId = countryid;
	    	
	    	// Assuming ListTypeValues has a method to get an Integer value
	    	Integer countryStatusValue = (countryStatus != null) ? countryStatus.getListTypeValueId() : null;
	    	
	        PostStudentResult result = Conservice.putCountry(lastUpdatedBy, countryCode, countryName,
	        		currencyCode, defaultCountry,maxPhoneNumberLength,currencySymbol, countryStatusValue,countryId);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	
	@PutMapping("/country/deactive/{countryid}/{listTypeValueId}")
	public ResponseEntity<PostStudentResult> putCountryStatus(@PathVariable(value = "countryid") Integer countryid,
			@PathVariable(value = "listTypeValueId") Integer listTypeValueId) {
	    try {
	    	Integer countryStatus = listTypeValueId;
	    	Integer countryId = countryid;
	    		
	        PostStudentResult result = Conservice.putCountryStatus(countryStatus,countryId);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        PostStudentResult errorResult = new PostStudentResult(); // Assuming PostStudentResult has appropriate fields
	        errorResult.setOutStatusCode(500);
	        errorResult.setOutMessage("An error occurred: " + e.getMessage());

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
	    }
	}
	
	//Multiple Insert
	@PostMapping("/insert")
    public ResponseEntity<List<PostStudentResult>> MultiinsertCountries(@RequestBody List<Country> countries) {
        try {
            List<PostStudentResult> results = Conservice.insertCountries(countries);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            // Log the error for debugging purposes
            logger.error("An error occurred while processing the request", e);

            // Return a meaningful error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(new PostStudentResult("Internal Server Error", 500)));
        }
    }
	
	//Multiple Table Insert
	@PostMapping("/postMultiTable")
	public ResponseEntity<PostStudentResult> postMultiTableController(
	        @RequestBody MultiTableRequest requestDTO) {

	    try {
	        // Extract values from DTO
	        Integer countryStatusValue = (requestDTO.getCountryStatus() != null) ? requestDTO.getCountryStatus().getListTypeValueId() : null;
	        Integer stateStatusValue = (requestDTO.getStateStatus() != null) ? requestDTO.getStateStatus().getListTypeValueId() : null;

	        // Call the service method
	        PostStudentResult result = Conservice.postMultiTable(
	                requestDTO.getCreatedBy(), requestDTO.getCountryCode(), requestDTO.getCountryName(),
	                requestDTO.getCurrencySymbol(), requestDTO.getCurrencyCode(), requestDTO.getDefaultCountry(),
	                requestDTO.getMaxPhoneNumberLength(), countryStatusValue,
	                requestDTO.getStateCode(), requestDTO.getStateName(), requestDTO.getStateTin(), stateStatusValue);

	        // Return the result with an OK status
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	
//	@PostMapping("/inserty")
//    public ResponseEntity<PostStudentResult> postMultiCountry(@RequestBody Country country) {
//        try {
//            List<String> createdBy = country.getCreatedBy();
//            List<String> countryCode = country.getCountryCode();
//            List<String> countryName = country.getCountryName();
//            List<String> currencySymbol = country.getCurrencySymbol();
//            List<String> currencyCode = country.getCurrencyCode();
//            List<Boolean> defaultCountry = country.isDefaultCountry();
//            List<String> maxPhoneNumberLength = country.getMaxPhoneNumberLength();
//            List<ListTypeValues> countryStatus = country.getCountryStatus();
//
//            // Assuming ListTypeValues has a method to get an Integer value
//            List<Integer> countryStatusValues = new ArrayList<>();
//
//            if (countryStatus != null) {
//                for (ListTypeValues status : countryStatus) {
//                    Integer statusValue = (status != null) ? status.getListTypeValueId() : null;
//                    countryStatusValues.add(statusValue);
//                }
//            }
//
//            PostStudentResult result = Conservice.postMultCountry(createdBy, countryCode, countryName, currencySymbol,
//                    currencyCode, defaultCountry, maxPhoneNumberLength, countryStatusValues);
//            
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            // Log the error for debugging purposes
//            logger.error("An error occurred while processing the request", e);
//            
//            // Return a meaningful error response
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PostStudentResult("Internal Server Error", 500));
//        }
//    }
	
//	@PostMapping("/insert")
//    public ResponseEntity<String> insertMultipleCountries(@RequestBody List<Country> country) {
//        try {
//        	Conservice.insertMultipleCountries(country);
//            return ResponseEntity.ok("Multiple countries inserted successfully.");
//        } catch (Exception e) {
//        	logger.error("An error occurred while processing the request", e);
//            // Handle exceptions or validation errors
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inserting multiple countries.");
//        }
//    }
	
	
	
	
//	@PostMapping("/insert")
//    public ResponseEntity<List<Country>> insertCountries(@RequestBody List<Country> countries) {
//        // Call the service method with the list of countries
//        List<Country> savedCountries = Conservice.insertMultCountries(countries);
//
//        // Return the saved countries
//        return ResponseEntity.ok(savedCountries);
//    }
	
//	@PostMapping("/insertMultCountries")
//    public ResponseEntity<String> insertMultCountries(@RequestBody InsertRequest request) {
//        // Assuming InsertRequest has createdBy and countries fields
//		Conservice.insertMultCountries(request.getCreatedBy(), request.getCountryCode(), request.getCountryName(), request.getCountries());
//        return ResponseEntity.ok("Multiple countries inserted successfully.");
//    }

}
