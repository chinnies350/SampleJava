package com.demo.api;


import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "list_type")
public class ListType extends AbstractAuditingEntity implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "list_type_id",unique = true, nullable = false, updatable = false)
	private Integer listTypeId;
	
	@Column(length = 200)
	private String description;
	
	@Column(name = "list_type_name", length = 200)
	private String listTypename;
	
	@Column 
	private Integer Status;
	
	@OneToMany(mappedBy = "listType", cascade = CascadeType.ALL)
	private List<ListTypeValues> listTypeValues;
	
	public ListType() {
		
	}
	
	

	public ListType(String description, String listTypename, Integer status, List<ListTypeValues> listTypeValues) {
		super();
		this.description = description;
		this.listTypename = listTypename;
		Status = status;
		this.listTypeValues = listTypeValues;
	}



	public List<ListTypeValues> getListTypeValues() {
		return listTypeValues;
	}

	public void setListTypeValues(List<ListTypeValues> listTypeValues) {
		this.listTypeValues = listTypeValues;
	}

	public Integer getListTypeId() {
		return listTypeId;
	}

	public void setListTypeId(Integer listTypeId) {
		this.listTypeId = listTypeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getListTypename() {
		return listTypename;
	}

	public void setListTypename(String listTypename) {
		this.listTypename = listTypename;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer status) {
		Status = status;
	}



	@Override
	public String toString() {
		return "ListType [listTypeId=" + listTypeId + ", description=" + description + ", listTypename=" + listTypename
				+ ", Status=" + Status + ", listTypeValues=" + listTypeValues + "]";
	}
	
	
	 
	

}
