package com.altqart.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "temp_stakeholder")
public class TempStakeholder {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@Column(name = "ref_stakeholder_id")
	private int refStakeholderId;

	private String name;

	@Column(name = "phone_no")
	private String phoneNo;

	private String email;

	private String description;

	private String note;

	private boolean active;

	private int approve;

	@Temporal(TemporalType.DATE)
	private Date date;

}
