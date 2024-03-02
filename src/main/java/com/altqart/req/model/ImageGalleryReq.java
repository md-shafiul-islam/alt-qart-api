package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageGalleryReq {

	private int id;
	
	private String name;

	private String altTag;

	private String title;

	private String location;

}
