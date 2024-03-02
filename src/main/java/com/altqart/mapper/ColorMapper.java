package com.altqart.mapper;

import java.util.List;
import java.util.Set;

import com.altqart.model.Color;
import com.altqart.model.ItemColor;
import com.altqart.req.model.ColorReq;
import com.altqart.resp.model.RespColor;
import com.altqart.resp.model.RespItemColor;

public interface ColorMapper {

	public Color mapColor(ColorReq colorReq);

	public RespColor mapRespColor(Color color);

	public List<RespColor> mapAllRespColor(List<Color> colors);

	public Set<RespItemColor> mapItemColor(List<ItemColor> colors);
	
	public RespItemColor mapRespItemColorOnly(ItemColor itemColor);

}
