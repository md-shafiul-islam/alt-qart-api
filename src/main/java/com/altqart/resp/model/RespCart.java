package com.altqart.resp.model;

import java.util.List;

import com.altqart.model.CartItem;
import com.altqart.model.Stakeholder;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

	private double dicountPar;

	private double discount;

	private String couponCode;

	private double totalAmount;

	private double totalQty;

	private boolean chooseAll;

}
