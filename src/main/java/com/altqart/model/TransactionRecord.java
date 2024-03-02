package com.altqart.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="transaction_record")
public class TransactionRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	@OneToOne(mappedBy = "transactionRecord")
	private EsCreditDetail creditDetail;
	
	@OneToOne
	@JoinColumn(name="debit_detail")
	private EsDebitDetail debitDetail;
	
	private double amount;
	
	private boolean paid;
	
	private boolean receive;
	
	private Date date;
	
	private Date dateGroup;
}
