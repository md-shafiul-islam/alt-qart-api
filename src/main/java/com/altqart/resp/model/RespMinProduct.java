package com.altqart.resp.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespMinProduct {

	private String id;

	private String aliasName;

	private String title;

	private String bnTitle;

	List<RespProductDescription> descriptions;

	private boolean discountStatus;

	private boolean upcoming;

	private String videoUrl;

	private String brand;

	private double avgRating;

	private Date updateDate;

	private RespMinVariant variant;
}
