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
@NoArgsConstructor
@AllArgsConstructor
public class RespProduct {

	private String id;

	private String aliasName;

	private String title;

	private String bnTitle;

	List<RespProductDescription> descriptions;

	private List<RespVariant> variants;
	
	private RespMeasurement measurement;

	private boolean discountStatus;

	private boolean upcoming;

	private String videoUrl;

	private RespCategory category;

	private Set<RespImageGallery> images;

	private Set<RespMetaData> metaDatas = new HashSet<>();

	private RespBrand brand;

	private Set<RespMaterial> materials = new HashSet<>();

	private List<RespSpecification> specifications;

	private List<RespRating> ratings;
	
	private double avgRating;

	private Date createDate;

	private Date updateDate;

}
