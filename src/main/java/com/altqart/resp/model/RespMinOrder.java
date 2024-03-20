package com.altqart.resp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.altqart.model.Stakeholder;
import com.altqart.model.Store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespMinOrder {

	private String id;

	private String invNo;

	private RespEsUser user;

	private RespStakeholder stakeholder;

	private RespStore store;

	private List<RespMinOrderItem> orderItems = new ArrayList<>();

	private RespShippingAddress shippingAddress;

	private RespStatus status;

	private String parcel;

	private double itemDiscount;

	private double totalAmount;

	private String couponCode;

	private double couponPar;

	private double couponDiscount;

	private double totalQty;

	private double subTotal;

	private double shippingAndHandlingAmount;

	private double shippingDiscount;

	private double grandTotal;

	private List<RespMethodAndTransaction> methodTransactions;

	private double paidAmount;

	private double creditAmount;

	private double codAmount;

	private double noteCost;

	private int returnStatus; // 0 pending, 1 Approve

	private Date date;

	private Date grupeDate;

	private String note;
}
