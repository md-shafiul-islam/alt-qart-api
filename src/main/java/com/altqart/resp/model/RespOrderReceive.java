package com.altqart.resp.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespOrderReceive {

	private double sumDiscount;

	private double sumAmountAfterDiscount;

	private BigDecimal sumOrderTotalProfit;

	private BigDecimal sumOrderTotalLoss;

	private double sumPayAmount;

	private Date grupeDate;

}
