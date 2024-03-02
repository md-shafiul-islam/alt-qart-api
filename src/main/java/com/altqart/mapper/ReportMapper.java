package com.altqart.mapper;

import java.util.List;

import com.altqart.model.OrderItem;
import com.altqart.resp.model.RespOrderItem;

public interface ReportMapper {

	public List<RespOrderItem> mapAllOrderItem(List<OrderItem> list);

}
