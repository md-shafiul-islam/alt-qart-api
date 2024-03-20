package com.altqart.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="public_id")
	private String publicId;
	
	@OneToOne
	@JoinColumn(name = "order", referencedColumnName = "id")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "parcel_status", referencedColumnName = "id")
	private ParcelStatus parcelStatus;

	@ManyToOne
	@JoinColumn(name = "parcel_provider", referencedColumnName = "id")
	private ParcelProvider parcelProvider;

	@JsonProperty("merchant_order_id")
	@Column(name = "merchant_order_id")
	private String merchantOrderId; // optinal parameter, merchant order info/tracking id Or Our Track Id

	@JsonProperty("consignment_id")
	@Column(name = "consignment_id")
	private String consignmentId; // is provided by Pathao and is the identifier code for the order"

	@JsonProperty("store_id")
	@Column(name = "store_id")
	private int storeId;

	@JsonProperty("sender_name")
	@Column(name = "sender_name")
	private String senderName;

	@JsonProperty("sender_phone")
	@Column(name = "sender_phone")
	private String senderPhone;

	@JsonProperty("recipient_name")
	@Column(name = "recipient_name")
	private String recipientName;

	@JsonProperty("recipient_phone")
	@Column(name = "recipient_phone")
	private String recipientPhone;

	@JsonProperty("recipient_address")
	@Column(name = "recipient_address")
	private String recipientAddress;

	@JsonProperty("recipient_city")
	@Column(name = "recipient_city")
	private int recipientCity;

	@JsonProperty("recipient_zone")
	@Column(name = "recipient_zone")
	private int recipientZone;

	@JsonProperty("recipient_area")
	@Column(name = "recipient_area")
	private int recipientArea;

	@JsonProperty("delivery_type")
	@Column(name = "delivery_type")
	private int deliveryType=48; // is provided by the merchant and not changeable. 48 for Normal Delivery, 12
								// for On Demand Delivery

	@JsonProperty("item_type")
	@Column(name = "item_type")
	private int itemType; // 1 for Document, 2 for Parcel

	@JsonProperty("special_instruction")
	@Column(name = "special_instruction")
	private String specialInstruction;

	@JsonProperty("item_quantity")
	@Column(name = "item_quantity")
	private double itemQuantity;

	@JsonProperty("item_weight")
	@Column(name = "item_weight")
	private double itemWeight;// <float, min:0.5, max:10.0> // is provided by the merchant and not changeable.

	@JsonProperty("amount_to_collect")
	@Column(name = "amount_to_collect")
	private int amountToCollect; // is provided by Merchant and not changeable. Default should be 0 in case of

	@JsonProperty("delivery_fee")
	@Column(name = "delivery_fee")
	private double deliveryFee;

	@JsonProperty("item_description")
	@Column(name = "item_description")
	private String itemDescription;
	
	private int status; //0 pending, 1 Send/Approve, 2 Cancel, 3 failed delivery, 4 delivered, 5 return

	private Date date;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_group")
	private Date dateGroup;

}
