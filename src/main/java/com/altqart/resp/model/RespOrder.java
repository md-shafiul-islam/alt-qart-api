package com.altqart.resp.model;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.altqart.model.MethodAndTransaction;
import com.altqart.model.OrderItem;
import com.altqart.model.Parcel;
import com.altqart.model.ShippingAddress;
import com.altqart.model.Stakeholder;
import com.altqart.model.Status;
import com.altqart.model.Store;
import com.altqart.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespOrder {

	private String id;

	private String invNo;

	private RespEsUser user;

	private Stakeholder stakeholder;

	private Store store;

	private List<RespOrderItem> orderItems = new ArrayList<>();

	private RespShippingAddress shippingAddress;

	private RespStatus status;

	private RespParcel parcel;

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
