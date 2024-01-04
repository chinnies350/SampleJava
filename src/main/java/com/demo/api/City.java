package com.demo.api;

import java.io.Serializable;

import com.demo.api.message.CityResponseEntity;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.CascadeType;
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
@Table(name = "loc_city")
public class City extends AbstractAuditingEntity implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "city_id", unique = true, nullable = false, updatable = false)
	private Integer cityId;
	
	@Column(name = "city_name", length=255)
	private String cityName;
	
	@ManyToOne
	@JoinColumn(name = "city_status")
	private ListTypeValues cityStatus;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "country_id")
	private Country countryId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "state_id")
	private State stateId;
	
	public City() {
		
	}
	
	

	public City(Integer cityId, String cityName, ListTypeValues cityStatus, Country country, State state) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
		this.cityStatus = cityStatus;
		this.countryId = country;
		this.stateId = state;
	}
	
//	public City(CityResponseEntity cityResponseEntity) {
//		
//		this.cityId = cityResponseEntity.getCityId();
//		this.cityName =cityResponseEntity.getCityName();
//		this.cityStatus =cityResponseEntity.getCityStatus();
//		this.country =cityResponseEntity.getCountry();
//		this.state =cityResponseEntity.getState();
//		this.createdBy=cityResponseEntity.getCreatedBy();
//		this.lastUpdatedBy=cityResponseEntity.getLastUpdatedBy();
//		this.createDateTime=cityResponseEntity.getCreateDateTime();
//		this.updateDateTime=cityResponseEntity.getUpdateDateTime();
//	}

	public void setCityStatusFromTuple(Tuple tuple) {
	    ListTypeValues cityStatus = new ListTypeValues();

	    // Extract the JSON object from the tuple at index 2
	    Object cityStatusObject = tuple.get(2);
	    
	    // Now, assuming the cityStatusObject is a JSON object, you need to convert it to the ListTypeValues entity.
	    // You may use a JSON library like Jackson for this conversion.

	    // Example using Jackson:
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	        // Convert the JSON object to ListTypeValues
	        cityStatus = objectMapper.convertValue(cityStatusObject, ListTypeValues.class);
	    } catch (Exception e) {
	        e.printStackTrace(); // Handle the exception appropriately
	    }

	    this.setCityStatus(cityStatus);
	}
	
	public void setCountryFromTuple(Tuple tuple) {
	    Country country = new Country();

	    Object countryObject = tuple.get(3);
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	    	country = objectMapper.convertValue(countryObject, Country.class);
	    } catch (Exception e) {
	        e.printStackTrace(); 
	    }

	    this.setCountry(country);
	}
	
	public void setStateFromTuple(Tuple tuple) {
	    State state = new State();

	    Object stateObject = tuple.get(4);
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	        // Convert the JSON object to ListTypeValues
	    	state = objectMapper.convertValue(stateObject, State.class);
	    } catch (Exception e) {
	        e.printStackTrace(); 
	    }

	    this.setState(state);
	}
	
//	public void setCountryFromTuple(Tuple tuple) {
//		System.out.println("Tuple"+tuple);
//	    Country country = new Country();
//	    Object stateIdObj = tuple.get("country_id");
//	    System.out.println("stateIdObj"+stateIdObj);
//	    if (stateIdObj instanceof Integer) {
//	    	country.setCountryId((Integer) stateIdObj);
//	    } else if (stateIdObj instanceof String) {
//	        // If the value is a String, try to parse it as an Integer
//	        try {
//	            country.setCountryId(Integer.parseInt((String) stateIdObj));
//	        } catch (NumberFormatException e) {
//	            // Handle the case where parsing fails (you can log an error or throw an exception)
//	            // For now, let's set it to a default value, replace this with your desired behavior
//	        	country.setCountryId(1);
//	        }
//	    } else {
//	        // Handle the case where the value is neither Integer nor String
//	        // For now, let's set it to a default value, replace this with your desired behavior
//	    	country.setCountryId(1);
//	    }
//	
//////	    Object stateIdObjCName = tuple.get("country_name");
////	    if (stateIdObj instanceof String) {
////	    	 System.out.println("stateIdObjCName"+stateIdObj);
////	         country.setCountryName((String) tuple.get("country_name"));
////	    }else {
////	    	System.out.println("stateIdObjCName else");
////	    }
//	   
//
//	    // Handle null values for default_country and convert to Boolean
////	    Boolean defaultCountry = tuple.get("default_country", Boolean.class);
////	    country.setDefaultCountry(Boolean.TRUE.equals(defaultCountry));
//
//	    this.setCountry(country);
//	}
	
//	public void setStateFromTuple(Tuple tuple) {
//		State state = new State();
//		state.setStateId(tuple.get("state_id",Integer.class));
//		state.setStateName(tuple.get("state_name",String.class));
//		
//		this.setStateId(state);
//	}
	
//	public void setStateFromTuple(Tuple tuple) {
//	    State state = new State();
//	    
//	    // Ensure that the retrieved value is an Integer
//	    Object stateIdObj = tuple.get("state");
//	    if (stateIdObj instanceof Integer) {
//	        state.setStateId((Integer) stateIdObj);
//	    } else if (stateIdObj instanceof String) {
//	        // If the value is a String, try to parse it as an Integer
//	        try {
//	            state.setStateId(Integer.parseInt((String) stateIdObj));
//	        } catch (NumberFormatException e) {
//	            // Handle the case where parsing fails (you can log an error or throw an exception)
//	            // For now, let's set it to a default value, replace this with your desired behavior
//	            state.setStateId(1);
//	        }
//	    } else {
//	        // Handle the case where the value is neither Integer nor String
//	        // For now, let's set it to a default value, replace this with your desired behavior
//	        state.setStateId(1);
//	    }
//	    
//	    state.setStateName(tuple.get("state_name", String.class));
//
//	    this.setState(state);
//	}



	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public ListTypeValues getCityStatus() {
		return cityStatus;
	}

	public void setCityStatus(ListTypeValues cityStatus) {
		this.cityStatus = cityStatus;
	}
	

//	public Country getCountryId() {
//		return countryId;
//	}
//
//
//
//	public void setCountryId(Country countryId) {
//		this.countryId = countryId;
//	}
//
//
//
//	public State getStateId() {
//		return stateId;
//	}
//
//
//
//	public void setStateId(State stateId) {
//		this.stateId = stateId;
//	}

	public Country getCountry() {
		return countryId;
	}



	public void setCountry(Country country) {
		this.countryId = country;
	}



	public State getState() {
		return stateId;
	}



	public void setState(State state) {
		this.stateId = state;
	}


	@Override
	public String toString() {
		return "City [cityId=" + cityId + ", cityName=" + cityName + ", cityStatus=" + cityStatus + ", countryId="
				+ countryId + ", stateId=" + stateId + "]";
	}
	
	
	
	

}
