package com.demo.api;

import java.io.Serializable;

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
@Table(name = "loc_country")
public class Country extends AbstractAuditingEntity implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "country_id" , unique = true, nullable = false, updatable = true)
	private Integer countryId;
	
	@Column(name = "country_code", length= 255)
	private String countryCode;
	
	@Column(name = "country_name", length= 255)
	private String countryName;
	
	@Column(name = "currency_symbol", length= 255)
	private String currencySymbol;
	
	@Column(name = "currency_code", length= 255)
	private String currencyCode;
	
	@Column(name = "default_country")
    private boolean  defaultCountry;
	
	@Column(name = "max_phonenumber_length", length= 255)
	private String maxPhoneNumberLength;
	
	@ManyToOne
	@JoinColumn(name = "country_status", nullable = true)
	private ListTypeValues countryStatus;
	
	public Country() {
		
	}
	

	public Country(Integer country, String countryCode, String countryName, String currencySymbol, String currencyCode,
			boolean defaultCountry, String maxPhoneNumberLength, ListTypeValues countryStatus) {
		super();
		this.countryId = country;
		this.countryCode = countryCode;
		this.countryName = countryName;
		this.currencySymbol = currencySymbol;
		this.currencyCode = currencyCode;
		this.defaultCountry = defaultCountry;
		this.maxPhoneNumberLength = maxPhoneNumberLength;
		this.countryStatus = countryStatus;
	}
	
//	public Country(Country country){
//		this.countryId = country.getCountryId();
//		this.countryName = country.getCountryName();
//		this.defaultCountry = country.isDefaultCountry();
////		this.countryCode = country.getCountryCode();
////		this.countrySymbol = country.getCountrySymbol();
////		this.currencyCode = country.getCurrencyCode();
//	}
//	
	public Country(String json) {
        if (json != null) {
            // Use Jackson to deserialize the JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            try {
            	Country values = objectMapper.readValue(json, Country.class);
                // Copy values to this instance
                this.countryId = values.countryId;
                this.countryName = values.countryName;
                this.defaultCountry = values.defaultCountry;
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
    }
	
	public Country(String json, String string) {
        if (json != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
            	Country values = objectMapper.readValue(json, Country.class);
                this.countryId = values.countryId;
                this.countryCode = values.countryCode;
                this.countryName = values.countryName;
                this.currencySymbol= values.currencySymbol;
                this.currencyCode = values.currencyCode;
                this.defaultCountry = values.defaultCountry;
                this.maxPhoneNumberLength = values.maxPhoneNumberLength;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
	
	public void setCountryStatusFromTuple(Tuple tuple) {
	    ListTypeValues countryStatus = new ListTypeValues();

	    // Extract the JSON object from the tuple at index 2
	    Object countryStatusObject = tuple.get(7);
	    
	    // Now, assuming the cityStatusObject is a JSON object, you need to convert it to the ListTypeValues entity.
	    // You may use a JSON library like Jackson for this conversion.

	    // Example using Jackson:
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	        // Convert the JSON object to ListTypeValues
	    	countryStatus = objectMapper.convertValue(countryStatusObject, ListTypeValues.class);
	    } catch (Exception e) {
	        e.printStackTrace(); // Handle the exception appropriately
	    }

	    this.setCountryStatus(countryStatus);
	}
	


	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}


	public String getCurrencySymbol() {
		return currencySymbol;
	}



	public String getCountryName() {
		return countryName;
	}


	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}


	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public boolean isDefaultCountry() {
		return defaultCountry;
	}

	public void setDefaultCountry(boolean defaultCountry) {
		this.defaultCountry = defaultCountry;
	}

	public String getMaxPhoneNumberLength() {
		return maxPhoneNumberLength;
	}

	public void setMaxPhoneNumberLength(String maxPhoneNumberLength) {
		this.maxPhoneNumberLength = maxPhoneNumberLength;
	}

	public ListTypeValues getCountryStatus() {
		return countryStatus;
	}

	public void setCountryStatus(ListTypeValues countryStatus) {
		this.countryStatus = countryStatus;
	}


	@Override
	public String toString() {
		return "Country [countryId=" + countryId + ", countryCode=" + countryCode + ", countryName=" + countryName
				+ ", currencySymbol=" + currencySymbol + ", currencyCode=" + currencyCode + ", defaultCountry="
				+ defaultCountry + ", maxPhoneNumberLength=" + maxPhoneNumberLength + "]";
	}
	
	


}
