package com.altqart.req.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsPaymentReq {

	private String payroll;

	private double amount;

	private String description;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date adjustmentDate;
}
