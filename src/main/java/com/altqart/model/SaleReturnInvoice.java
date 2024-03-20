package com.altqart.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sale_return_invoice")
public class SaleReturnInvoice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@Column(name = "invoice_id")
	private String invoiceId;

	@ManyToOne
	@JoinColumn(name = "store", referencedColumnName = "id")
	private Store store;

	@ManyToOne
	@JoinColumn(name = "stakeholder", referencedColumnName = "id")
	private Stakeholder stakeholder;
	
	@ManyToOne
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User approveUser;
		

	@OneToMany(mappedBy = "saleReturnInvoice")
	private List<SaleReturnItem> returnItems;

	@OneToMany(mappedBy = "saleReturnInvoice")
	private List<MethodAndTransaction> methodAndTransactions;
	
	@OneToOne(mappedBy = "saleReturnInvoice")	
	private EsDebitDetail debitDetail;

	@ManyToOne
	@JoinColumn(name = "return_type", referencedColumnName = "id")
	private ReturnType returnType;

	@Column(name = "items_total")
	private double itemsTotal;

	@Column(name = "return_fees")
	private double returnFees;

	@Column(name = "sub_return_fees")
	private double subReturnFees;
	
	@Column(name = "less_adustment")
	private double lessAdustment;

	@Column(name = "grand_total")
	private double grandTotal;

	@Column(name = "paid_amount")
	private double paidAmount;

	@Column(name = "credit_amount")
	private double creditAmount;

	@Column(name = "change_total")
	private double changeAmount;

	@Column(name = "total_profit")
	private double totalProfit;

	@Column(name = "total_loss")
	private double totalLoss;

	@Column(name = "total_discount")
	private double totalDiscount;

	@Column(name = "total_vat")
	private double totalVat;

	@Column(name="prev_credit")
	private double prevCredit;
	
	@Column(name="prev_debit")
	private double prevDebit;
	
	@Column(name = "note")
	private String note;

	@Column(name = "reject_note")
	private String rejectNote;

	private int approve;

	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name = "date_group")
	@Temporal(TemporalType.DATE)
	private Date groupDate;

	@Column(name = "sale_revenue")
	private double saleRevenue;

}
