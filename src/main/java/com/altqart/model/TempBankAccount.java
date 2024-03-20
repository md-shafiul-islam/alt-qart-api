package com.altqart.model;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="temp_bank_account")
public class TempBankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="ref_id")
	private String refId;
	
	private String name;

	@Column(name="account_type")
	private int accountType;
	
	@Column(name="monthly_installment")
	private double monthlyInstallment;
	
	private boolean active;
	
}
