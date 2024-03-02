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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parcel")
public class Parcel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@OneToOne
	@JoinColumn(name = "order", referencedColumnName = "id")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "parcel_status")
	private ParcelStatus parcelStatus;

	@ManyToOne
	@JoinColumn(name = "parcel_provider", referencedColumnName = "id")
	private ParcelProvider parcelProvider;

	@Column(name = "track_no")
	private String trackNo;

	@Column(name = "merchant_order_id")
	private String merchantOrderId; // optinal parameter, merchant order info/tracking id Or Our Track Id

	@Column(name = "consignment_id")
	private String consignmentId; // Provider Id

	private String address;

	@Column(name = "store_id")
	private int storeId;

	@Column(name = "sender_name")
	private String senderName;

	@Column(name = "sender_phone")
	private String senderPhone;

	@Column(name = "recipient_name")
	private String recipientName;

	@Column(name = "recipient_phone")
	private String recipientPhone;

	@Column(name = "recipient_address")
	private String recipientAddress;

	@Column(name = "recipient_city")
	private int recipientCity;

	@Column(name = "recipient_zone")
	private int recipientZone;

	@Column(name = "recipient_area")
	private int recipientArea;

	@Column(name = "delivery_type")
	private int deliveryType; // is provided by the merchant and not changeable. 48 for Normal Delivery, 12
								// for On Demand Delivery

	@Column(name = "item_type")
	private String itemType; // 1 for Document, 2 for Parcel

	@Column(name = "special_instruction")
	private String specialInstruction;

	@Column(name = "item_quantity")
	private String itemQuantity;

	@Column(name = "item_weight")
	private double itemWeight;// <float, min:0.5, max:10.0> // is provided by the merchant and not changeable.
								// Minimum 0.5 KG to Maximum 10 kg

	@Column(name = "amount_to_collect")
	private int amountToCollect; // is provided by Merchant and not changeable. Default should be 0 in case of
									// NON Cash-On-Delivery(COD)

	@Column(name = "delivery_fee")
	private double deliveryFee;

	@Column(name = "item_description")
	private String itemDescription;

	private Date date;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_group")
	private Date dateGroup;

}
