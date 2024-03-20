package com.altqart.mapper;

import java.util.List;

import com.altqart.model.Order;
import com.altqart.req.model.OrderPlaceReq;
import com.altqart.req.model.OrderReq;
import com.altqart.resp.model.RespMinOrder;
import com.altqart.resp.model.RespOrder;

public interface SaleMapper {

	public Order mapOrder(OrderReq orderReq);

	public List<RespOrder> mapAllOrder(List<Order> orders);

	public RespOrder mapRespOrderOnly(Order order);

	public RespOrder mapOrderDetails(Order order);

	public List<RespOrder> mapAllOrderOnly(List<Order> orders);

	public List<RespOrder> mapAllOrderByStatus(List<Order> orders, int status);

	public RespOrder mapSaleInvoiceDetails(Order dbOrder);

	public Order mapPlaceOrder(OrderPlaceReq orderPlaceReq);

	public List<RespMinOrder> mapAllRespMinOrder(List<Order> orders);

}
