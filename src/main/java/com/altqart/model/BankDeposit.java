package com.altqart.model;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bank_deposit")
public class BankDeposit {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="public_id")
	private String publicId;
	
	@ManyToOne
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "account", referencedColumnName = "id")
	private BankAccount account;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "deposit")
	private BankTransection bankTransection;
	
	@Column(name = "recepit_no")
	private String recepitNo;
	
	@Column(name = "phone_no")
	private String phoneNo;
	
	@Column(name = "deposited_by")
	private String depositedBy;
	
	@Column(name = "note")
	private String note;
	
	@Column(name = "amount")
	private double amount;
	
	@Column(name="approve")
	private int approve;
	
	private Date date;
	
	@Column(name = "date_groupe")
	@Temporal(TemporalType.DATE)
	private Date dateGroup;
			
	
}
