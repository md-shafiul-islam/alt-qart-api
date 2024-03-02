package com.altqart.mapper;

import java.util.List;

import com.altqart.model.Rating;
import com.altqart.resp.model.RespRating;

public interface RatingMapper {

	public List<RespRating> mapAllRespRating(List<Rating> ratings);
	
	public RespRating mapRespRating(Rating rating);
}
