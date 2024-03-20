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
@Table(name = "paid_record")
public class Paid {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@Column(name = "inv_id")
	private String invId;

	@ManyToOne
	@JoinColumn(name = "stakeholder", referencedColumnName = "id")
	private Stakeholder stakeholder;

	@OneToMany(mappedBy = "paid")
	private List<MethodAndTransaction> methodAndTransactions;

	@ManyToOne
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;

	@Column(name = "prev_credit")
	private double prevCredit;

	@Column(name = "present_credit")
	private double presentCredit;

	private double amount;

	private double discount;

	@Column(name = "grand_total")
	private double grandTotal;

	private String description;

	private String rejectNote;

	private int approve; // 0,1,2

	@Column(name = "date_group")
	@Temporal(TemporalType.DATE)
	private Date dateGroup;

	@Temporal(TemporalType.DATE)
	private Date date;

	@Transient
	private String payableId;

	@Transient
	private int transTypeId;

}
