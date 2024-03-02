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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "es_debit_detail")
public class EsDebitDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@ManyToOne
	@JoinColumn(name = "debit", referencedColumnName = "id")
	private EsDebit debit;

	@Column(name = "bank_debit")
	private int bankAccountDebit;

	@OneToOne
	@JoinColumn(name = "es_credit_detail", referencedColumnName = "id")
	private EsCreditDetail esCreditDetail;

	@OneToOne(mappedBy = "debitDetail")
	private TransactionRecord transactionRecord;

	@OneToOne
	@JoinColumn(name = "sale_return", referencedColumnName = "id")
	private SaleReturnInvoice saleReturnInvoice;

	@ManyToOne
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;

	@Column(name = "amount")
	private double amount;

	private String note;

	private int status; // 0 not set, 1 add, 2 adjustment

	@Column(name = "drawing")
	private boolean drawing;

	private Date date;

	@Column(name = "date_group")
	@Temporal(TemporalType.DATE)
	private Date dateGroup;

}
