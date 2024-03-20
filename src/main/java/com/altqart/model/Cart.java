package com.altqart.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
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
@Table(name = "cart")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@OneToOne
	@JoinColumn(name = "stakeholder", referencedColumnName = "id")
	private Stakeholder stakeholder;

	@OneToMany(mappedBy = "cart")
	@OrderBy("id ASC")
	private List<CartItem> cartItems;

	private double discount;

	@Column(name = "total_amount")
	private double totalAmount;

	@Column(name = "choose_amount")
	private double chooseAmount;

	@Column(name = "choose_qty")
	private double chooseQty;

	@Column(name = "coupon_code")
	private String couponCode;

	@Column(name = "coupon_par")
	private double couponPar;

	@Column(name = "coupon_discount")
	private double couponDiscount;

	@Column(name = "grand_total")
	private double grandTotal;

	@Column(name = "total_qty")
	private double totalQty;

	@Column(name = "choose_all")
	private boolean choose = false;

	private Date date;

	@Column(name = "update_date")
	private Date updateDate;

}
