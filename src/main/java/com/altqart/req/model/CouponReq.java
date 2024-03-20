package com.altqart.req.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponReq {

	private String code;

	private double applyAmount; // -1 is any amount;

	private double applyQty; // -1 is any amount;

	private double amount;// 0-any

	private double percentage; // 0-100

	private double count;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expireDate;
}
