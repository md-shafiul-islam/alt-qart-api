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
@Table(name = "es_credit_detail")
public class EsCreditDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@ManyToOne
	@JoinColumn(name = "credit", referencedColumnName = "id")
	private EsCredit credit;

	@Column(name = "bank_credit")
	private int bankAccountCredit;

	@ManyToOne
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;

	@OneToOne
	@JoinColumn(name = "order", referencedColumnName = "id")
	private Order order;

	@Column(name = "amount")
	private double amount;

	@OneToOne
	@JoinColumn(name = "transaction_record", referencedColumnName = "id")
	private TransactionRecord transactionRecord;

	private String note;

	private Date date;

	private int status; // 0 not set, 1 add, 2 adjustment

	@Column(name = "date_group")
	@Temporal(TemporalType.DATE)
	private Date dateGroup;

}
