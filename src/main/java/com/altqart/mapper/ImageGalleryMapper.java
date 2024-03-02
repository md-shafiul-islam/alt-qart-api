package com.altqart.mapper;

import java.util.Set;

import com.altqart.model.ImageGallery;
import com.altqart.resp.model.RespImageGallery;

public interface ImageGalleryMapper {

	public Set<RespImageGallery> mapAllRespImage(Set<ImageGallery> images);
	
	public RespImageGallery mapRespImage(ImageGallery image);
	
	public RespImageGallery mapRespImageOnly(ImageGallery image);

}
