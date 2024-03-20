package com.altqart.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@Column(name = "tracking_no")
	private String trackingNo;

	@Column(name = "inv_no")
	private String invNo;

	@ManyToOne
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "stakeholder", referencedColumnName = "id")
	private Stakeholder stakeholder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store", referencedColumnName = "id")
	private Store store;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
	private List<OrderItem> orderItems = new ArrayList<>();

	@OneToOne
	@JoinColumn(name = "shipping_address", referencedColumnName = "id")
	private ShippingAddress shippingAddress;

	@ManyToOne
	@JoinColumn(name = "status", referencedColumnName = "id")
	private Status status;

	@OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
	private Parcel parcel;

	@Column(name = "item_discount")
	private double itemDiscount;

	@Column(name = "total_amount")
	private double totalAmount;

	@Column(name = "coupon_code")
	private String couponCode;

	@Column(name = "coupon_par")
	private double couponPar;

	@Column(name = "coupon_discount")
	private double couponDiscount;

	@Column(name = "total_qty")
	private double totalQty;

	@Column(name = "sub_total")
	private double subTotal;

	@Column(name = "shipping_amount")
	private double shippingAndHandlingAmount;

	@Column(name = "shipping_discount")
	private double shippingDiscount;

	@Column(name = "grand_total")
	private double grandTotal;

	@OneToMany(mappedBy = "order")
	private List<MethodAndTransaction> methodTransactions;

	@Column(name = "paid_amount")
	private double paidAmount;

	@Column(name = "credit_amount")
	private double creditAmount;

	@Column(name = "cod_amount")
	private double codAmount;

	@Column(name = "note_cots")
	private double noteCost;

	private int returnStatus; // 0 pending, 1 Approve

	@Column(name = "parcel_create")
	private boolean parcelCreate;

	private Date date;

	@Column(name = "group_date")
	@Temporal(TemporalType.DATE)
	private Date grupeDate;

	private String note;

}
