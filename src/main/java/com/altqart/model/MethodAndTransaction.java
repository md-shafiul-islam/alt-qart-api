package com.altqart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "method_and_transaction")
public class MethodAndTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@ManyToOne
	@JoinColumn(name = "source", referencedColumnName = "id")
	private BankAccount source;

	@ManyToOne
	@JoinColumn(name = "destination", referencedColumnName = "id")
	private BankAccount destination;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_return", referencedColumnName = "id")
	private SaleReturnInvoice saleReturnInvoice;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale", referencedColumnName = "id")
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paid", referencedColumnName = "id")
	private Paid paid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receive", referencedColumnName = "id")
	private Receive receive;


	@Column(name = "ref_no")
	private String refNo;

	private double amount;
}
