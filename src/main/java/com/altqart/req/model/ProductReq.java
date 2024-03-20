package com.altqart.req.model;

import java.util.List;

import com.altqart.model.ProductDescription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReq {

	private String id;

	private List<MetaDataReq> metaDatas;

	private String title;

	private String originCode;

	private String bnTitle;

	private String aliasName;

	private List<ImageGalleryReq> images;

	private String video;

	private int category;

	private String brand;

	private List<Integer> materials;

	private List<ProductDescriptionReq> descriptions;

	private List<VariantReq> variants;

	private List<SpecificationReq> specifications;
	
	private List<ItemColorReq> itemColorReqs;

	private int measurement;

	private boolean discountStatus;

	private boolean upcoming;

}
