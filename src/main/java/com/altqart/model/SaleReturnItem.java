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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sale_return_item")
public class SaleReturnItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	private String name;

	@ManyToOne
	@JoinColumn(name = "sale_return", referencedColumnName = "id")
	private SaleReturnInvoice saleReturnInvoice;

	@OneToOne
	@JoinColumn(name = "order_item", referencedColumnName = "id")
	private OrderItem orderItem;

	private double qty;

	private double price;

	@Column(name = "item_sub")
	private double itemSub;

	@Column(name = "discount_rate")
	private double discountRate;

	@Column(name = "dsicount")
	private double dsicount;

	@Column(name = "sub_dsicount")
	private double subDsicount;


	@Column(name = "sub_total")
	private double subTotal;

	private double profit;

	private double loss;

	@Column(name="total_profit")
	private double totalProfit;

	@Column(name="total_loss")
	private double totalLoss;

	@Column(name = "date")
	@Temporal(TemporalType.DATE)
	private Date date;

}
