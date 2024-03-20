package com.altqart.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", referencedColumnName = "id", insertable = true, updatable = true, nullable = true)
	private Order order;

	@ManyToOne
	@JoinColumn(name = "variant", referencedColumnName = "id")
	private Variant variant;

	private double qty;

	private double price;
	
	private boolean isReturn;
	
	private double returnQty;

	@Column(name = "sub_total_item")
	private double subTotal;

	@Column(name = "exp_warranty")
	@Temporal(TemporalType.DATE)
	private Date expWarranty;

	private Date date;

	@Column(name = "date_group")
	@Temporal(TemporalType.DATE)
	private Date groupDate;

}
