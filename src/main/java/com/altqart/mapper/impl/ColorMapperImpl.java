package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.ColorMapper;
import com.altqart.model.Color;
import com.altqart.model.ItemColor;
import com.altqart.req.model.ColorReq;
import com.altqart.resp.model.RespColor;
import com.altqart.resp.model.RespItemColor;

@Service
public class ColorMapperImpl implements ColorMapper {

	@Autowired
	private HelperServices helperServices;

	@Override
	public Color mapColor(ColorReq colorReq) {

		if (colorReq != null) {
			Color color = new Color();

			if (!helperServices.isNullOrEmpty(colorReq.getValue())) {
				color.setCKey(colorReq.getValue());
			} else {
				color.setCKey(helperServices.getKey(colorReq.getName()));
			}

			color.setName(colorReq.getName());

			return color;
		}
		return null;
	}

	@Override
	public RespColor mapRespColor(Color color) {

		if (color != null) {
			RespColor respColor = new RespColor();
			respColor.setId(color.getId());
			respColor.setName(color.getName());
			respColor.setValue(color.getCKey());

			return respColor;
		}

		return null;
	}

	@Override
	public List<RespColor> mapAllRespColor(List<Color> colors) {

		if (colors != null) {
			List<RespColor> respColors = new ArrayList<>();

			for (Color color : colors) {
				RespColor respColor = mapRespColor(color);

				if (respColor != null) {
					respColors.add(respColor);
				}
			}

			return respColors;
		}
		return null;
	}

	@Override
	public Set<RespItemColor> mapItemColor(List<ItemColor> colors) {

		if (colors != null) {

			Set<RespItemColor> itemColors = new HashSet<>();

			for (ItemColor color : colors) {
				RespItemColor itemColor = mapRespItemColorOnly(color);

				if (itemColor != null) {
					itemColors.add(itemColor);
				}
			}

			return itemColors;
		}

		return null;
	}

	@Override
	public RespItemColor mapRespItemColorOnly(ItemColor itemColor) {

		if (itemColor != null) {
			RespItemColor color = new RespItemColor();

			if (itemColor.getColor() != null) {
				color.setName(itemColor.getColor().getName());
				color.setId(itemColor.getColor().getId());
				color.setImageUrl(itemColor.getUrl());
				color.setValue(itemColor.getColor().getCKey());
			}

			color.setImageUrl(itemColor.getUrl());

			return color;
		}
		return null;
	}

}
