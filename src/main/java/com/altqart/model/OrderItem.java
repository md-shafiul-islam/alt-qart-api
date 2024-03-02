package com.altqart.model;

import java.math.BigDecimal;
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
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", referencedColumnName = "id", insertable = true, updatable = true, nullable = true)
	private Order order;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product", referencedColumnName = "id")
	private Product product;

	private double qty;

	private double price;

	@Column(name = "sub_total_item")
	private BigDecimal subTotal;

	@Column(name = "profit")
	private double profit;

	@Column(name = "total_profit")
	private double totalProfit;

	@Column(name = "loss")
	private double loss;

	@Column(name = "total_loss")
	private double totalLoss;

	@Column(name = "item_cost_og")
	private double totalCostOfGoods;

	@Column(name = "exp_warranty")
	@Temporal(TemporalType.DATE)
	private Date expWarranty;

	private Date date;

	@Column(name = "date_group")
	@Temporal(TemporalType.DATE)
	private Date groupDate;

	@Column(name = "purchase_id")
	private String purchaseBarCode;

	@Column(name = "return_status")
	private int returnStatus;

	@Column(name = "return_qty")
	private double returnQty;

}
