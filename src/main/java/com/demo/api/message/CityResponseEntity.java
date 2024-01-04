package com.demo.api.message;

import java.io.IOException;

import java.time.LocalDateTime;

import com.demo.api.City;
import com.demo.api.Country;
import com.demo.api.ListTypeValues;
import com.demo.api.State;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Tuple;

@Entity

public class CityResponseEntity {
	
	@Id
    private Integer cityId;
    private String cityName;
    private ListTypeValues cityStatus;
    private Country country;
    private State state;
    private String createdBy;
    private LocalDateTime createDateTime;
    private String lastUpdatedBy;
    private LocalDateTime updateDateTime;
    
    
        
  
	public CityResponseEntity() {
		
   	}
    
	public CityResponseEntity(CityResponseEntity cityResponseEntity) {
			
			this.cityId = cityResponseEntity.getCityId();
			this.cityName =cityResponseEntity.getCityName();
			this.cityStatus =cityResponseEntity.getCityStatus();
			this.country =cityResponseEntity.getCountry();
			this.state =cityResponseEntity.getState();
			this.createdBy=cityResponseEntity.getCreatedBy();
			this.lastUpdatedBy=cityResponseEntity.getLastUpdatedBy();
			this.createDateTime=cityResponseEntity.getCreateDateTime();
			this.updateDateTime=cityResponseEntity.getUpdateDateTime();
		}
	
	public void setCityStatusFromTuple(Tuple tuple) {
        ListTypeValues cityStatus = new ListTypeValues();
        cityStatus.setListTypeValueId(tuple.get("listTypeValueId", Integer.class));
        cityStatus.setList_type_value_name(tuple.get("listTypeValueName", String.class));
        cityStatus.setStatus(tuple.get("status", Integer.class));

        this.setCityStatus(cityStatus);
    }
	
	public void setCountryFromTuple(Tuple tuple) {
        Country country = new Country();
        country.setCountryId(tuple.get("countryId", Integer.class));
        country.setCountryName(tuple.get("countryName", String.class));
        country.setDefaultCountry(tuple.get("defaultCountry", Boolean.class));

        this.setCountry(country);
    }
	
	public void setStateFromTuple(Tuple tuple) {
		State state = new State();
		state.setStateId(tuple.get("stateId",Integer.class));
		state.setStateName(tuple.get("stateName",String.class));
		
		this.setState(state);
	}
	
	public CityResponseEntity(City result) {
		this.cityId = result.getCityId();
		this.cityName =result.getCityName();
		this.cityStatus =result.getCityStatus();
//		this.country =result.getCountryId();
//		this.state =result.getStateId();
		this.createdBy=result.getCreatedBy();
		this.lastUpdatedBy=result.getLastUpdatedBy();
		this.createDateTime=result.getCreateDateTime();
		this.updateDateTime=result.getUpdateDateTime();
	}
	
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
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}
	public LocalDateTime getUpdateDateTime() {
		return updateDateTime;
	}
	public void setUpdateDateTime(LocalDateTime updateDateTime) {
		this.updateDateTime = updateDateTime;
	}
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	


	


   
}
