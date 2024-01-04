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
import org.springframework.web.bind.annotation.RestController;

import com.demo.api.City;
import com.demo.api.Country;
import com.demo.api.ListTypeValues;
import com.demo.api.PostStudentResult;
import com.demo.api.State;
import com.demo.api.service.StateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/email")
public class StateController {
	
	@Autowired StateService Stservice;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(StateController.class);
	
	@GetMapping("/state")
	public ResponseEntity<?> getState() {

	    try {
	        List<State> stateList = Stservice.getStateData(null,null,null,null,null);
	        if (!stateList.isEmpty()) {
	            return ResponseEntity.ok(stateList);
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
	
	@GetMapping("/state/{id}")
	public ResponseEntity<?> getStateById(@PathVariable("id") Integer id) {

	    try {
	        List<State> stateList = Stservice.getStateData(id,null,null,null,null);
	        if (!stateList.isEmpty()) {
	            return ResponseEntity.ok(stateList.get(0));
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message","State With Id: " + id + " Not Found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	        }
	    } catch (Exception e) {
	        // Log the exception for debugging purposes
	        logger.error("An error occurred while processing the request", e);

	        // Provide a more meaningful response to the client
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
	    }
	}
	
	@GetMapping("/state/state_status/{status}")
	public ResponseEntity<?> getStateByStatus(@PathVariable("status") Integer status) {

	    try {
	        List<State> stateList = Stservice.getStateData(null,status,null,null,null);
	        if (!stateList.isEmpty()) {
	            return ResponseEntity.ok(stateList);
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message","State With status: " + status + " Not Found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	        }
	    } catch (Exception e) {
	        // Log the exception for debugging purposes
	        logger.error("An error occurred while processing the request", e);

	        // Provide a more meaningful response to the client
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
	    }
	}
	
	@GetMapping("/state/active_state")
	public ResponseEntity<?> getStateByActiveState() {

	    try {
	    	String mode  = "Active";
	        List<State> stateList = Stservice.getStateData(null,null,mode,null,null);
	        if (!stateList.isEmpty()) {
	            return ResponseEntity.ok(stateList);
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message"," No Data Found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	        }
	    } catch (Exception e) {
	        // Log the exception for debugging purposes
	        logger.error("An error occurred while processing the request", e);

	        // Provide a more meaningful response to the client
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
	    }
	}
	
	@GetMapping("/state/countryId/{country}")
	public ResponseEntity<?> getStateByCountryId(@PathVariable("country") Integer country) {

	    try {
	        List<State> stateList = Stservice.getStateData(null,null,null,country,null);
	        if (!stateList.isEmpty()) {
	            return ResponseEntity.ok(stateList);
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message","State With countryId: " + country + " Not Found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	        }
	    } catch (Exception e) {
	        // Log the exception for debugging purposes
	        logger.error("An error occurred while processing the request", e);

	        // Provide a more meaningful response to the client
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
	    }
	}
	
	@GetMapping("/state/stateName/{stateName}")
	public ResponseEntity<?> getStateBystateName(@PathVariable("stateName") String stateName) {

	    try {
	        List<State> stateList = Stservice.getStateData(null,null,null,null,stateName);
	        if (!stateList.isEmpty()) {
	            return ResponseEntity.ok(stateList);
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message","State With stateName: " + stateName + " Not Found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	        }
	    } catch (Exception e) {
	        // Log the exception for debugging purposes
	        logger.error("An error occurred while processing the request", e);

	        // Provide a more meaningful response to the client
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
	    }
	}
	
	@GetMapping("/state/countryId/{country}/{stateName}")
	public ResponseEntity<?> getStateByCountryAndstateName(@PathVariable("country") Integer country,
			@PathVariable("stateName") String stateName) {

	    try {
	        List<State> stateList = Stservice.getStateByCountryStateName(country,stateName);
	        if (!stateList.isEmpty()) {
	            return ResponseEntity.ok(stateList);
	        } else {
	            ObjectNode jsonObject = objectMapper.createObjectNode();
	            jsonObject.put("statusCode", 404);
	            jsonObject.put("message"," No Data Found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	        }
	    } catch (Exception e) {
	        // Log the exception for debugging purposes
	        logger.error("An error occurred while processing the request", e);

	        // Provide a more meaningful response to the client
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
	    }
	}
	
	@PutMapping("/state/{stateid}")
	public ResponseEntity<?> putState(@RequestBody State state, @PathVariable Integer stateid) {
	    try {
	        String lastUpdatedBy = state.getLastUpdatedBy();
	        String stateCode = state.getStateCode();
	        String stateName = state.getStateName();
	        String stateTin = state.getStateTin();
	        Country countryId = state.getCountry();
	        ListTypeValues stateStatus = state.getStateStatus();
	        Integer stateId = stateid;

	        Integer stateStatusValue = (stateStatus != null) ? stateStatus.getListTypeValueId() : null;
	        Integer countryIdValue = (countryId != null) ? countryId.getCountryId() : null;

	        PostStudentResult result = Stservice.putState(lastUpdatedBy, stateCode, stateName, stateTin,
	        		countryIdValue,stateStatusValue,stateId);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
	    }
	}
	
	@PutMapping("/state/deactive/{stateid}/{listTypeValueId}")
	public ResponseEntity<?> putStateStatus(@PathVariable(value = "stateid") Integer stateid,
			@PathVariable(value = "listTypeValueId") Integer listTypeValueId) {
	    try {
	        Integer stateStatus = listTypeValueId;
	        Integer stateId = stateid;


	        PostStudentResult result = Stservice.putStateStatus(stateStatus,stateId);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
	    }
	}
	
	
	
	@PostMapping("/state")
	public ResponseEntity<PostStudentResult> postState(@RequestBody State state) {
	    try {
	    	String createdBy = state.getCreatedBy();
	    	String stateCode = state.getStateCode();
	    	String stateName = state.getStateName();
	    	String stateTin = state.getStateTin();
	    	Country countryId = state.getCountry();
	    	ListTypeValues stateStatus = state.getStateStatus();
	    	
	    	// Assuming ListTypeValues has a method to get an Integer value
	    	Integer countryIdValue = (countryId != null) ? countryId.getCountryId() : null;
	    	
	    	Integer stateStatusValue = (stateStatus != null) ? stateStatus.getListTypeValueId() : null;
	    	
	        PostStudentResult result = Stservice.callStateProcedure(createdBy, stateCode, stateName,stateTin,
	        		countryIdValue,stateStatusValue);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}

}
