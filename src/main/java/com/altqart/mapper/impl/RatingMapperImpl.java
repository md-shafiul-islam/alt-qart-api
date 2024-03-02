package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.RatingMapper;
import com.altqart.mapper.UserMapper;
import com.altqart.model.Rating;
import com.altqart.resp.model.RespRating;
import com.altqart.resp.model.RespUser;

@Service
public class RatingMapperImpl implements RatingMapper{
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public List<RespRating> mapAllRespRating(List<Rating> ratings) {
		
		if(ratings != null) {
			
			List<RespRating> respRatings = new ArrayList<>();
			
			for (Rating rating : ratings) {
				RespRating respRating = mapRespRating(rating);
				
				if(respRating != null) {
					respRatings.add(respRating);
				}
			}
			
			return respRatings;
			
		}
		
		return null;
	}
	
	@Override
	public RespRating mapRespRating(Rating rating) {

		if(rating != null) {
						
			RespRating respRating = new RespRating();
			respRating.setContent(rating.getContent());
			respRating.setDate(rating.getDate());
			respRating.setId(rating.getPublicId());
			respRating.setRateScore(rating.getRateScore());
			
			
			if(rating.getAuthor() != null) {
				RespUser respUser = userMapper.mapRespUserOnly(rating.getAuthor());
				respRating.setAuthor(respUser);
			}
			
			return respRating;
		}
		
		return null;
	}
}
