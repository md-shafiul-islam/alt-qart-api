package com.altqart.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.altqart.model.Order;
import com.altqart.model.OrderItem;
import com.altqart.model.Status;
import com.altqart.model.User;
import com.altqart.req.model.ReqDate;
import com.altqart.resp.model.RespOrder;

public interface OrderServices {

	public Order getOrderById(int id);

	public List<Status> getAllOrdertatus();

	public Status getOrdertatusById(int statusId);

	public long getSaleCount();

	public boolean saveOrderViaAPI(Order order);

	public Map<String, Object> approveOrder(Order order, int i);

	public Order getOrderByPublicId(String id);

	public List<RespOrder> getAllOrderDelivered(int startAt, int pageSize);

	public List<RespOrder> getAllProcessesOrder(int startAt, int pageSize);

	public List<RespOrder> getAllOrderPending(int startAt, int pageSize);

	public RespOrder getOrderDetailsByPublicId(String id);

	public List<RespOrder> getOrderByBetweenDateAndStatus(ReqDate reqDate);

	public List<RespOrder> getOrderByMonthAndStatus(int status, Date date);

	public List<RespOrder> getThisDayOrAnyOrderByStatus(int status, Date date);

	public List<RespOrder> getThisOrAnyWeekOrderByStatus(int status, Date date);

	public List<RespOrder> getThisMonthOrAnyOrderByStatus(int status, Date date);

	public int getThisDayOrderCount(Date date);

	public void getSaleInvoiceDetailsByPublicId(String id, Map<String, Object> map);

	public OrderItem getOrderItemById(int orderItem);

	

}
