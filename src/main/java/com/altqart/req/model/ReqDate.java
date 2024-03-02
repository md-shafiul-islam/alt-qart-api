package com.altqart.req.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqDate {

	@JsonFormat(pattern="yyyy-MM-dd")
	private Date date;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	
	private int status;
}
