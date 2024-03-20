package com.altqart.resp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespParcel {

	private String id;

	private RespOrder order;

	private RespParcelStatus parcelStatus;

	private RespMinParcelProvider parcelProvider;

	private String merchantOrderId; // optinal parameter, merchant order info/tracking id Or Our Track Id

	private String consignmentId; // is provided by Pathao and is the identifier code for the order"

	private int storeId;

	private String senderName;

	private String senderPhone;

	private String recipientName;

	private String recipientPhone;

	private String recipientAddress;

	private int recipientCity;

	private int recipientZone;

	private int recipientArea;

	private String deliveryType; // is provided by the merchant and not changeable. 48 for Normal Delivery, 12
									// for On Demand Delivery

	private String itemType; // 1 for Document, 2 for Parcel

	private String specialInstruction;

	private double itemQuantity;

	private double itemWeight;// <float, min:0.5, max:10.0> // is provided by the merchant and not changeable.

	private int amountToCollect; // is provided by Merchant and not changeable. Default should be 0 in case of

	private double deliveryFee;

	private String itemDescription;

	private int status; // 0 pending, 1 Send/Approve, 2 Cancel, 3 failed delivery, 4 delivered, 5 return

	private Date date;

	private Date dateGroup;
}
