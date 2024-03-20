package com.altqart.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "bank_paid")
public class BankPaid {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@ManyToOne
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "stakeholder", referencedColumnName = "id")
	private Stakeholder stakeholder;

	@ManyToOne
	@JoinColumn(name = "giver_account", referencedColumnName = "id")
	private BankAccount giverBankAccount;

	@ManyToOne
	@JoinColumn(name = "receiver_account", referencedColumnName = "id")
	private BankAccount receiverBankAccount;

	@Column(name = "amount")
	private double amount;

	@Column(name = "cheque_no")
	private String chequeNo;

	@Column(name = "receipt_no")
	private String receiptNo;

	@Column(name = "transaction_id")
	private String transactionID; // If Online Transaction

	@Column(name = "note")
	private String note;

	@Column(name = "phone_no")
	private String phoneNo;

	@Column(name = "posted_by")
	private String postedBy;

	@Column(name = "approve")
	private int approve;

	private Date date;

	@Column(name = "date_group")
	@Temporal(TemporalType.DATE)
	private Date dateGroup;
}
