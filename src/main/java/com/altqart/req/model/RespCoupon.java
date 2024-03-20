package com.altqart.req.model;

import java.util.Date;

import com.altqart.model.User;
import com.altqart.resp.model.RespUser;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespCoupon {

	private String id;

	private String code;

	private RespUser user;

	private double applyAmount; // -1 is any amount;

	private double applyQty; // -1 is any amount;

	private double amount;// 0-any

	private double percentage; // 0-100

	private boolean active;

	private boolean valid;

	private double count;

	private Date expireDate;

	private Date createDate;

}
