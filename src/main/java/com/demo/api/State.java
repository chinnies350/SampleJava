package com.demo.api;

import java.io.Serializable;

import javax.print.DocFlavor.STRING;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Tuple;

@SuppressWarnings("serial")
@Entity
@Table(name = "loc_state")
public class State extends AbstractAuditingEntity implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "state_id", unique = true, nullable = false, updatable = false)
	private Integer stateId;
	
	@Column(name = "state_code", length=255)
	private String stateCode;
	
	@Column(name = "state_name", length=255)
	private String stateName;
	
	@Column(name = "state_tin", length=255)
	private String stateTin;
	
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country countryId;
	
	@ManyToOne
	@JoinColumn(name = "state_status")
	private ListTypeValues stateStatus;
	
	public State() {
	
	}
	

	public State(Integer stateId, String stateCode, String stateName, String stateTin, Country countryId,
			ListTypeValues stateStatus) {
		super();
		this.stateId = stateId;
		this.stateCode = stateCode;
		this.stateName = stateName;
		this.stateTin = stateTin;
		this.countryId = countryId;
		this.stateStatus = stateStatus;
	}
	
	public State(State state) {
		 this.stateId = state.getStateId();
//		 this.stateName = state.getStateName();
//		 this.stateCode = state.getStateCode();
	}
	
	public State(String json) {
        if (json != null) {
            // Use Jackson to deserialize the JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            try {
            	State values = objectMapper.readValue(json, State.class);
                // Copy values to this instance
                this.stateId = values.stateId;
                this.stateName = values.stateName;
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
    }
	
	public void setCountryFromTuple(Tuple tuple) {
	    Country country = new Country();

	    // Extract the JSON object from the tuple at index 2
	    Object countryStatusObject = tuple.get(4);
	    
	    try {
	        // Convert the JSON object to ListTypeValues
	    	country = new Country(countryStatusObject.toString(), null);
	    } catch (Exception e) {
	        e.printStackTrace(); // Handle the exception appropriately
	    }

	    this.setCountry(country);
	}
	
	public void setStateStatusFromTuple(Tuple tuple) {
	    ListTypeValues stateStatus = new ListTypeValues();

	    // Extract the JSON object from the tuple at index 2
	    Object countryStatusObject = tuple.get(5);
	    
	    // Now, assuming the cityStatusObject is a JSON object, you need to convert it to the ListTypeValues entity.
	    // You may use a JSON library like Jackson for this conversion.

	    // Example using Jackson:
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	        // Convert the JSON object to ListTypeValues
	    	stateStatus = objectMapper.convertValue(countryStatusObject, ListTypeValues.class);
	    } catch (Exception e) {
	        e.printStackTrace(); // Handle the exception appropriately
	    }

	    this.setStateStatus(stateStatus);
	}
	


	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateTin() {
		return stateTin;
	}

	public String getStateName() {
		return stateName;
	}


	public void setStateName(String stateName) {
		this.stateName = stateName;
	}


	public void setStateTin(String stateTin) {
		this.stateTin = stateTin;
	}

	public Country getCountry() {
		return countryId;
	}

	public void setCountry(Country country) {
		this.countryId = country;
	}

	public ListTypeValues getStateStatus() {
		return stateStatus;
	}

	public void setStateStatus(ListTypeValues stateStatus) {
		this.stateStatus = stateStatus;
	}


	@Override
	public String toString() {
		return "State [stateId=" + stateId + ", stateCode=" + stateCode + ", stateName=" + stateName + ", stateTin="
				+ stateTin + ", countryId=" + countryId + ", stateStatus=" + stateStatus + "]";
	}
	
	

}
