package com.altqart.resp.model;

import java.util.Date;
import java.util.List;

import com.altqart.model.CartItem;
import com.altqart.model.Stakeholder;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespCart {

	private String id;

	private String stakeholder;

	private List<RespCartItem> cartItems;

	private double totalWeight;

	private double discount;

	private String couponCode;

	private double totalAmount;

	private double totalQty;

	private boolean chooseAll;

	private double chooseAmount;

	private double chooseQty;

	private double couponPar;

	private double couponDiscount;

	private double grandTotal;

	private boolean choose = false;

	private Date date;

	private Date updateDate;

}
