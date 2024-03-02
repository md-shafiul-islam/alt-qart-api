package com.altqart.model;

import java.util.Date;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="bank_credit_record")
public class BankAccountCreditRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@ManyToOne
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "credit", referencedColumnName = "id")
	private BankAccountCredit accountCredit;

	@OneToOne
	@JoinColumn(name = "es_debit_detail", referencedColumnName = "id")
	private EsDebitDetail esDebitDetail;
	
	@OneToOne
	@JoinColumn(name = "debit_record", referencedColumnName = "id")
	private BankAccountDebitRecord accountDebitRecord;
	
	@OneToOne
	@JoinColumn(name="method_transaction", referencedColumnName = "id")
	private MethodAndTransaction methodTransaction;

	private String note;

	private double amount;

	@Column(name="is_count")
	private boolean count; //If Bank account amount is count as cash
	
	private Date date;

	@Column(name = "date_group")
	@Temporal(TemporalType.DATE)
	private Date dateGroup;
}
