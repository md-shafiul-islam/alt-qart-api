
package com.altqart.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "stakeholder")
public class Stakeholder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "gen_id")
	private String genId;
	
	@Column(name = "public_id")
	private String publicId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;

	@OneToOne(mappedBy = "stakeholder")
	private Cart cart;

	@OneToMany(mappedBy = "stakeholder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Order> orders = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "stakeholder", fetch = FetchType.LAZY)
	private List<SaleReturnInvoice> saleReturnInvoices = new ArrayList<>();

	@Column(name = "description")
	private String description;

	@Column(name = "reject_note")
	private String rejectNote;

	@Column(name = "active")
	private boolean active;

	@ManyToMany
	@JoinTable(name = "stakeholder_type", joinColumns = { @JoinColumn(name = "stack_holder") }, inverseJoinColumns = {
			@JoinColumn(name = "stack_type") })
	private Set<StakeholderType> stakeholderTypes = new HashSet<>();

	@Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "stakeholder", fetch = FetchType.LAZY)
	List<Address> addresses;

	@Column(name = "email")
	private String email;

	@Column(name = "phone_no")
	private String phoneNo;

	@OneToMany(mappedBy = "stakeholder", fetch = FetchType.LAZY)
	private List<BankAccount> bankAccounts;

	/**
	 * As customer receivable and business payable amount Or Creditor Amount
	 */
	@Column(name = "total_credit_amount")
	private BigDecimal totalCreditAmount;

	/**
	 * As customer payable and business receivable amount Or Debtor Amount
	 */
	@Column(name = "total_debit_amount")
	private BigDecimal totalDebitAmount;

	@OneToOne(mappedBy = "stakeholder")
	private EsCredit credit;

	@OneToOne(mappedBy = "stakeholder")
	private EsDebit debit;

	@Transient
	private double creditBalance;

	@Transient
	private double debitBalance;

	private String note;

	@Column(name = "approve")
	private int approve; // 0, 1, 2 value Pending, Approve, Reject,

	private boolean updateReq;

	private String updateNote;

	@Temporal(TemporalType.DATE)
	@Column(name = "date")
	private Date date;

}
