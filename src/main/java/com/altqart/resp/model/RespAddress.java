package com.altqart.resp.model;

import java.util.List;

import com.altqart.model.Area;
import com.altqart.model.City;
import com.altqart.model.ShippingAddress;
import com.altqart.model.Stakeholder;
import com.altqart.model.Upazila;
import com.altqart.model.Zone;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespAddress {

	private String id;
	
	private String fullName;

	private RespStakeholder stakeholder;

	private String city;

	private String zone;

	private String area;

	private boolean isDefault;

	private boolean isOffice;

	private String zipCode;

}
