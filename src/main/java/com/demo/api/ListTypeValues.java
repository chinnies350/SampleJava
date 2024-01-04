package com.demo.api;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

@SuppressWarnings("serial")
@Entity
@Table(name = "list_type_values")

public class ListTypeValues extends AbstractAuditingEntity implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "list_type_value_id", unique = true, nullable = false, updatable = false)
	private Integer listTypeValueId;
	
	@Column(length = 255)
	private String description;
	
	@Column(length = 255)
	private String list_type_value_name;
	
	@Column 
	private Integer Status;
	
	@ManyToOne
	@JoinColumn(name = "list_types_list_type_id")
	private ListType listType;
	
	public ListTypeValues() {
		
	}
	
	

	public ListTypeValues(Integer listTypeValueId, String description, String list_type_value_name, Integer status,
			ListType listType) {
		super();
		this.listTypeValueId = listTypeValueId;
		this.description = description;
		this.list_type_value_name = list_type_value_name;
		this.Status = status;
		this.listType = listType;
	}
	
//	public ListTypeValues(ListTypeValues listTypeValues) {
//		super();
//		this.listTypeValueId = listTypeValues.getListTypeValueId();
//		this.list_type_value_name = listTypeValues.getList_type_value_name();
////		this.description = listTypeValues.getDescription();
//		this.Status = listTypeValues.getStatus();
//	}
	
//	public ListTypeValues(String listTypeValueName, boolean someFlag) {
//	    this.list_type_value_name = listTypeValueName;
//	}
	
	
	
	public ListTypeValues(String json) {
        if (json != null) {
            // Use Jackson to deserialize the JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ListTypeValues values = objectMapper.readValue(json, ListTypeValues.class);
                // Copy values to this instance
                this.listTypeValueId = values.listTypeValueId;
                this.list_type_value_name = values.list_type_value_name;
                this.Status = values.Status;
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
    }
	
	
	
//	public ListTypeValues(Integer listTypeValueId, String listTypeValueName, Integer status) {
//        this.listTypeValueId = listTypeValueId;
//        this.list_type_value_name = listTypeValueName;
//        this.Status = status;
//    }

	
//	public ListTypeValues(int status) {
//	    this.Status = status;
//	}



	public Integer getListTypeValueId() {
		return listTypeValueId;
	}

	public void setListTypeValueId(Integer listTypeValueId) {
		this.listTypeValueId = listTypeValueId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getList_type_value_name() {
		return list_type_value_name;
	}

	public void setList_type_value_name(String list_type_value_name) {
		this.list_type_value_name = list_type_value_name;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer status) {
		Status = status;
	}

	public ListType getListType() {
		return listType;
	}

	public void setListType(ListType listType) {
		this.listType = listType;
	}



	@Override
	public String toString() {
		return "ListTypeValues [listTypeValueId=" + listTypeValueId + ", description=" + description
				+ ", list_type_value_name=" + list_type_value_name + ", Status=" + Status + ", listType=" + listType
				+ "]";
	}



	
	
	
	

}
