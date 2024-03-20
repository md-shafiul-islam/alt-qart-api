package com.altqart.model;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@Table(name = "bank_account")
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@ManyToOne
	@JoinColumn(name = "branch", referencedColumnName = "id")
	private BankBranch bankBranch;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "account_store", joinColumns = {
			@JoinColumn(name = "account", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "store", referencedColumnName = "id") })
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stakeholder", referencedColumnName = "id")
	private Stakeholder stakeholder;

	private String name;

	@Column(name = "account_no")
	private String accountNo;

	@ManyToOne
	@JoinColumn(name = "type", referencedColumnName = "id")
	private BankAccountType type;

	@Column(name = "total_debit")
	private double totalDebit;

	@Column(name = "total_credit")
	private double totalCredit;

	@Column(name = "debit_balance")
	private double debitBalance;

	@Column(name = "credit_balance")
	private double creditBalance;

	@OneToOne(mappedBy = "bankAccount", fetch = FetchType.LAZY)
	private BankAccountCredit credit;

	@OneToOne(mappedBy = "bankAccount", fetch = FetchType.LAZY)
	private BankAccountDebit debit;

	@Column(name = "note")
	private String note;

	private int approve;

	private boolean active;

	private boolean owner;

	@Column(name = "update_req")
	private boolean updateReq;

	private Date date;

	@Column(name = "date_group")
	@Temporal(TemporalType.DATE)
	private Date dateGroup;

}
