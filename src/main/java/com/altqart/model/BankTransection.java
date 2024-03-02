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
@Table(name = "bank_transection")
public class BankTransection {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@ManyToOne
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "account", referencedColumnName = "id")
	private BankAccount account;

	@OneToOne
	@JoinColumn(name = "bank_deposit", referencedColumnName = "id")
	private BankDeposit deposit;

	@OneToOne
	@JoinColumn(name = "bank_withdraw", referencedColumnName = "id")
	private BankWithdraw bankWithdraw;

	@Column(name = "amount")
	private double amount;

	@ManyToOne
	@JoinColumn(name = "status", referencedColumnName = "id")
	private BankStatus bankStatus;

	@Column(name = "token")
	private String token;

	@Column(name = "note")
	private String note;

	private int pay;

	private int receive;

	private Date date;

	@Column(name = "date_group")
	@Temporal(TemporalType.DATE)
	private Date dateGroup;

}
