package com.altqart.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name="bank_credit")
public class BankAccountCredit {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@OneToOne
	@JoinColumn(name = "bank_account", referencedColumnName = "id")
	private BankAccount bankAccount;

	@OneToOne
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;
	

	@OneToMany(mappedBy = "accountCredit")
	private List<BankAccountCreditRecord> bankAccountCreditRecords;

	private double amount;

	private String description;

	private Date date;
	
}
