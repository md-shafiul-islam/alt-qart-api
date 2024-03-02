package com.altqart.resp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespRating {

	private String id;

	private RespUser author;

	private RespProduct product;

	private double rateScore;

	private String content;

	private Date date;
}
