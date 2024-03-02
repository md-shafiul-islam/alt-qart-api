package com.altqart.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "daily_statistics")
public class DailyStatistics {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@ManyToOne
	@JoinColumn(name = "store", referencedColumnName = "id")
	private Store store;

	@Column(name = "stock")
	private double stock;

	@Column(name = "sales_revenue")
	private double salesRevenue;

	@Column(name = "sales_paid")
	private double salesPaid;

	@Column(name = "sales_due")
	private double salesDue;

	@Column(name = "sales_profit")
	private double salesProfit;

	@Column(name = "sales_loss")
	private double salesLoss;

	@Column(name = "daily_cash_debit")
	private double dailyCashDebit;

	@Column(name = "daily_cash_credit_balance")
	private double dailyCashCreditBalance;

	@Column(name = "daily_cash_debit_balance")
	private double dailyCashDebitBalance;

	@Column(name = "total_cash_ac_credit")
	private double totalCashAcCredit;

	@Column(name = "total_cash_ac_credit_balance")
	private double totalCashAcCreditBalance;

	@Column(name = "total_cash_ac_debit")
	private double totalCashAcDebit;

	@Column(name = "total_cash_ac_debit_balance")
	private double totalCashAcDebitBalance;

	@Column(name = "total_escredit")
	private double totalEsCredit;

	@Column(name = "total_escredit_balance")
	private double totalEsCreditBalance;

	@Column(name = "total_esdebit")
	private double totalEsDebit;

	@Column(name = "total_esdebit_balance")
	private double totalEsDebitBalance;

	@Column(name = "daily_total_escredit")
	private double dailyTotalEsCredit;

	@Column(name = "daily_total_escredit_balance")
	private double dailyTotalEsCreditBalance;

	@Column(name = "daily_total_esdebit")
	private double dailyTotalEsDebit;

	@Column(name = "daily_total_esdebit_balance")
	private double dailyTotalEsDebitBalance;

	@Column(name = "total_discount_credit")
	private double totalDiscountCredit;

	@Column(name = "discount_credit_balance")
	private double discountCreditBalance;

	@Column(name = "total_discount_debit")
	private double totalDiscountDebit;

	@Column(name = "discount_debit_balance")
	private double discountDebitBalance;

	@Column(name = "closing_balance")
	private double closeingBalance;

	@Column(name = "opening_balance")
	private double openingBalance;

	@Column(name = "sales_service_profit")
	private double salesServiceProfit;

	@Column(name = "sales_service_loss")
	private double salesServiceLoss;

	@Column(name = "date")
	private Date date;

	@Transient
	private String textDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "date_group")
	@Temporal(TemporalType.DATE)
	private Date dateGroup;

	@Column(name = "finalize")
	private boolean finalize;
}
