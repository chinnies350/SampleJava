package com.demo.api.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.hibernate.mapping.Map;
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
import com.demo.api.ListTypeValues;
import com.demo.api.PostStudentResult;
import com.demo.api.State;
import com.demo.api.message.CityResponseEntity;
import com.demo.api.publisher.RabbitMqJsonProducer;
import com.demo.api.repository.CityRepository;
import com.demo.api.service.CityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import jakarta.persistence.Tuple;

@RestController
@RequestMapping("/email")
public class CityController {
	
	@Autowired
	private CityService cityservice;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
    private CityRepository cityRepository;
	
	@Autowired
	private RabbitMqJsonProducer rabbitMqJsonProducer;
	
	private static final Logger logger = LoggerFactory.getLogger(CityController.class);
	
//	@GetMapping("/city")
//	public ResponseEntity<List<CityResponseEntity>> getCity(
//	        @RequestParam(name = "id", required = false) Integer id,
//	        @RequestParam(name = "status", required = false) Integer status) {
//
//	    try {
//	        List<CityResponseEntity> cityList = cityservice.getCity(id, status);
//	        return ResponseEntity.ok(cityList);
//	    } catch (Exception e) {
//	        // Log the exception for debugging purposes
//	    	logger.error("An error occurred while processing the request", e);
//
//	        // Provide a more meaningful response to the client
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
//	    }
//	}
	
	@GetMapping(value = "/city", produces = "application/json")
	public ResponseEntity<String> getCities() {
	    try {
	    	List<City> cityList = cityservice.getCityData(null, null, null,null,null);
	        if (!cityList.isEmpty()) {
	            // Send cities to RabbitMQ
	            rabbitMqJsonProducer.sendCities(cityList);

	            // Convert cityList to JSON format
	            ArrayNode cityArrayNode = objectMapper.createArrayNode();
	            for (City city : cityList) {
	                // Convert each city to JSON format as needed
	                ObjectNode cityNode = objectMapper.createObjectNode();
	                cityNode.put("createDateTime", city.getCreateDateTime().toString());
	                cityNode.put("createdBy", city.getCreatedBy());
	                cityNode.put("cityId", city.getCityId());
	                cityNode.put("cityName", city.getCityName());

	                // Include additional information about cityStatus, state, and country
	                ObjectNode cityStatusNode = objectMapper.createObjectNode();
	                cityStatusNode.put("listTypeValueId", city.getCityStatus().getListTypeValueId());
	                cityStatusNode.put("list_type_value_name", city.getCityStatus().getList_type_value_name());
	                cityStatusNode.put("status", city.getCityStatus().getStatus());
	                cityNode.set("cityStatus", cityStatusNode);

	                ObjectNode stateNode = objectMapper.createObjectNode();
	                stateNode.put("stateId", city.getState().getStateId());
	                stateNode.put("stateName", city.getState().getStateName());
	                cityNode.set("state", stateNode);

	                ObjectNode countryNode = objectMapper.createObjectNode();
	                countryNode.put("countryId", city.getCountry().getCountryId());
	                countryNode.put("countryName", city.getCountry().getCountryName());
	                countryNode.put("defaultCountry", city.getCountry().isDefaultCountry());
	                cityNode.set("country", countryNode);

	                // Add cityNode to the array
	                cityArrayNode.add(cityNode);
	            }


	         // Return the response to the client with the Content-Type header set to application/json
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_JSON);
	            return new ResponseEntity<>(cityArrayNode.toString(), headers, HttpStatus.OK);
	            
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message", "No Data");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject.toString());
	        }
	    } catch (Exception e) {
	        // Log the exception for debugging purposes
	        logger.error("An error occurred while processing the request", e);

	        // Provide a more meaningful response to the client
	        ObjectNode jsonObject = objectMapper.createObjectNode();
	        jsonObject.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
	        jsonObject.put("message", "Internal Server Error");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject.toString());
	    }
	}
	
//	@GetMapping("/city")
//	public ResponseEntity<?> getCity() {
//
//	    try {
//	        List<City> cityList = cityservice.getCityData(null, null, null,null,null);
//	        if (!cityList.isEmpty()) {
//	        	System.out.println("cityList" + cityList);
//	        	// Send the data to RabbitMQ queue
//	        	rabbitMqJsonProducer.sendCities(cityList);
//	        	
//	            return ResponseEntity.ok(cityList);
//	        } else {
//	            ObjectNode jsonObject = objectMapper.createObjectNode();
//	            jsonObject.put("statusCode", 404);
//	            jsonObject.put("message", "No Data");
//	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
//	        }
//	    } catch (Exception e) {
//	        // Log the exception for debugging purposes
//	        logger.error("An error occurred while processing the request", e);
//
//	        // Provide a more meaningful response to the client
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
//	    }
//	}
	
	@GetMapping("/city/{id}")
	public ResponseEntity<?> getCityById(@PathVariable Integer id) {
	    try {
	        List<City> cities = cityservice.getCityData(id, null, null,null,null);
	
	        if (!cities.isEmpty()) {
	            return ResponseEntity.ok(cities.get(0));
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message", "City Id: " + id + " Not Found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	        }
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@GetMapping("/city/city_status/{status}")
	public ResponseEntity<?> getCityByStatus(@PathVariable Integer status) {
	    try {
	        List<City> cities = cityservice.getCityData(null, status, null,null,null);
	
	        if (!cities.isEmpty()) {
	            return ResponseEntity.ok(cities);
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message","status" + status + " Not Found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	        }
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@GetMapping("/city/active_city")
	public ResponseEntity<?> getCityByactiveciy() {
	    try {
	    	String mode  = "Active";
	        List<City> cities = cityservice.getCityData(null, null, mode,null,null);
	
	        if (!cities.isEmpty()) {
	            return ResponseEntity.ok(cities);
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
	
	@GetMapping("/city/cityName/{cityName}")
	public ResponseEntity<?> getCityByCityName(@PathVariable String cityName) {
	    try {
	        List<City> cities = cityservice.getCityData(null, null, null,cityName,null);
	
	        if (!cities.isEmpty()) {
	            return ResponseEntity.ok(cities);
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message",cityName + " Not Found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	        }
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@GetMapping("/city/state/{state}")
	public ResponseEntity<?> getCityByStatee(@PathVariable Integer state) {
	    try {
	        List<City> cities = cityservice.getCityData(null, null, null,null,state);
	
	        if (!cities.isEmpty()) {
	            return ResponseEntity.ok(cities);
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message","stateId "+ state + " Not Found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	        }
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@PostMapping("/city")
	public ResponseEntity<?> postCity(@RequestBody City city) {
	    try {
	        String createdBy = city.getCreatedBy();
	        String cityName = city.getCityName();
	        ListTypeValues cityStatus = city.getCityStatus();
	        Country countryId = city.getCountry();
	        State stateId = city.getState();

	        Integer cityStatusValue = (cityStatus != null) ? cityStatus.getListTypeValueId() : null;
	        Integer countryIdValue = (countryId != null) ? countryId.getCountryId() : null;
	        Integer stateIdValue = (stateId != null) ? stateId.getStateId() : null;

	        PostStudentResult result = cityservice.postCity(createdBy, cityName, cityStatusValue, countryIdValue,
	                stateIdValue);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
	    }
	}
	
	
	@PutMapping("/city/{cityid}")
	public ResponseEntity<?> putCity(@RequestBody City city, @PathVariable Integer cityid) {
	    try {
	        String lastUpdatedBy = city.getLastUpdatedBy();
	        String cityName = city.getCityName();
	        ListTypeValues cityStatus = city.getCityStatus();
	        Country countryId = city.getCountry();
	        State stateId = city.getState();
	        Integer cityId = cityid;

	        Integer cityStatusValue = (cityStatus != null) ? cityStatus.getListTypeValueId() : null;
	        Integer countryIdValue = (countryId != null) ? countryId.getCountryId() : null;
	        Integer stateIdValue = (stateId != null) ? stateId.getStateId() : null;

	        PostStudentResult result = cityservice.putCity(lastUpdatedBy, cityName, cityStatusValue, countryIdValue,
	                stateIdValue,cityId);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
	    }
	}
	
	@PutMapping("/city/deactive/{cityid}/{listTypeValueId}")
	public ResponseEntity<?> putCityStatus(@RequestBody City city, @PathVariable(value = "cityid") Integer cityid,
	        @PathVariable(value = "listTypeValueId") Integer listTypeValueId) {
	    try {
	        Integer cityStatus = listTypeValueId;
	        Integer cityId = cityid;

	        PostStudentResult result = cityservice.putCityStatus( cityStatus, cityId);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
	    }
	}
	
	
	@PutMapping("/city/listTypeValueNamedeactive/{cityid}/{listTypeValueName}")
	public ResponseEntity<?> putCityListTypeValueName(@RequestBody City city, @PathVariable(value = "cityid") Integer cityid,
			@PathVariable(value ="listTypeValueName") String listTypeValueName) {
	    try {
	        String cityStatus = listTypeValueName;
	        Integer cityId = cityid;

	        PostStudentResult result = cityservice.putCityListTypeName( cityStatus, cityId);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
	    }
	}
	
	
	
//	@PostMapping("/city")
//	public ResponseEntity<PostStudentResult> postCity(@RequestBody City city) {
//	    try {
//	    	String createdBy = city.getCreatedBy();
//	    	String cityName = city.getCityName();
//	    	ListTypeValues cityStatus = city.getCityStatus();
//	    	Country countryId = city.getCountry();
//	    	State stateId = city.getState();
//	    	
//	    	// Assuming ListTypeValues has a method to get an Integer value
//	    	Integer cityStatusValue = (cityStatus != null) ? cityStatus.getListTypeValueId() : null;
//	    	
//	    	Integer countryIdValue = (countryId != null) ? countryId.getCountryId() : null;
//	    	
//	    	Integer stateIdValue = (stateId != null) ? stateId.getStateId() : null;
//	    	
//	        PostStudentResult result = cityservice.postCity(createdBy, cityName, cityStatusValue,countryIdValue,
//	        		stateIdValue);
//	        return new ResponseEntity<>(result, HttpStatus.OK);
//	    } catch (Exception e) {
//	        logger.error("An error occurred while processing the request", e);
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//	    }
//	}

}
