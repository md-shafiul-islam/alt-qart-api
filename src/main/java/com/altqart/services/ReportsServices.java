package com.altqart.services;

import java.util.List;

import com.altqart.resp.model.RespOrderItem;

public interface ReportsServices {

	public List<RespOrderItem> getCurrentDayOrderItems();

}
