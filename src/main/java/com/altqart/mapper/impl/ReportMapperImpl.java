package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.altqart.mapper.ReportMapper;
import com.altqart.model.OrderItem;
import com.altqart.resp.model.RespOrderItem;

@Service
public class ReportMapperImpl implements ReportMapper {

	@Override
	public List<RespOrderItem> mapAllOrderItem(List<OrderItem> list) {

		if (list != null) {
			List<RespOrderItem> items = new ArrayList<>();
			for (OrderItem orderItem : list) {
				RespOrderItem respOrderItem = mapRepOrderItem(orderItem);

				if (respOrderItem != null) {
					items.add(respOrderItem);
				}
			}

			return items;
		}

		return null;
	}

	public RespOrderItem mapRepOrderItem(OrderItem orderItem) {
		if (orderItem != null) {
			RespOrderItem items = new RespOrderItem();
			items.setDate(orderItem.getDate());
			items.setQty(orderItem.getQty());
			items.setPrice(orderItem.getPrice());

			return items;
		}
		return null;
	}
}
