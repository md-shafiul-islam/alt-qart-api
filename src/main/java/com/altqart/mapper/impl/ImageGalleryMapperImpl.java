package com.altqart.mapper.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.altqart.mapper.ImageGalleryMapper;
import com.altqart.model.ImageGallery;
import com.altqart.resp.model.RespImageGallery;

@Service
public class ImageGalleryMapperImpl implements ImageGalleryMapper {

	@Override
	public Set<RespImageGallery> mapAllRespImage(Set<ImageGallery> images) {
		
		if(images != null) {
			
			Set<RespImageGallery> galleries = new HashSet<>();
			
			for (ImageGallery imageGallery : images) {
				RespImageGallery gallery = mapRespImageOnly(imageGallery);
				
				if(gallery != null) {
					galleries.add(gallery);
				}
			}
			
			return galleries;
		}
		
		return null;
	}
	
	@Override
	public RespImageGallery mapRespImage(ImageGallery image) {
		
		if(image != null) {
			
			RespImageGallery gallery = mapRespImageOnly(image);
			
			if(gallery != null) {
				//TODO: Details
			}
			
			return gallery;
		}
		
		return null;
	}
	
	@Override
	public RespImageGallery mapRespImageOnly(ImageGallery image) {
		
		if(image != null) {
			RespImageGallery gallery = new RespImageGallery();
			gallery.setAltTag(image.getAltTag());
			gallery.setId(image.getId());
			gallery.setLocation(image.getLocation());
			gallery.setName(image.getName());
			gallery.setTitle(image.getTitle());
			
			return gallery;
		}
		
		return null;
	}
	
}
