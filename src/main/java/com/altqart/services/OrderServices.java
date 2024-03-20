package com.altqart.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.altqart.model.Order;
import com.altqart.model.OrderItem;
import com.altqart.model.Status;
import com.altqart.req.model.OrderPlaceReq;
import com.altqart.req.model.ReqDate;
import com.altqart.resp.model.RespMinOrder;
import com.altqart.resp.model.RespOrder;

public interface OrderServices {

	public Order getOrderById(int id);

	public List<Status> getAllOrdertatus();

	public Status getOrdertatusById(int statusId);

	public void updateOrderStatus(String id, String status, Map<String, Object> map);

	public long getSaleCount();

	public Map<String, Object> approveOrder(String order);

	public Order getOrderByPublicId(String id);

	public List<RespMinOrder> getAllOrderDelivered(int startAt, int pageSize);

	public List<RespMinOrder> getAllProcessesOrder(int startAt, int pageSize);

	public List<RespMinOrder> getAllOrderPending(int startAt, int pageSize);

	public RespOrder getOrderDetailsByPublicId(String id);

	public List<RespOrder> getOrderByBetweenDateAndStatus(ReqDate reqDate);

	public List<RespOrder> getOrderByMonthAndStatus(int status, Date date);

	public List<RespOrder> getThisDayOrAnyOrderByStatus(int status, Date date);

	public List<RespOrder> getThisOrAnyWeekOrderByStatus(int status, Date date);

	public List<RespOrder> getThisMonthOrAnyOrderByStatus(int status, Date date);

	public int getThisDayOrderCount(Date date);

	public void getSaleInvoiceDetailsByPublicId(String id, Map<String, Object> map);

	public OrderItem getOrderItemById(int orderItem);

	public List<RespOrder> getAll(int start, int siz);

	public void getOrderPlace(OrderPlaceReq orderPlaceReq, Map<String, Object> map);

	public void getSaleReturnApprove(String id, Map<String, Object> map);

	public void getOrderFaileldDeliveryApprove(String id, Map<String, Object> map);

	public List<RespMinOrder> getAllOrderShipped(int start, int size);

	public List<RespMinOrder> getAllOrderCanceled(int start, int size);

	public List<RespMinOrder> getAllOrderFailedDelivery(int start, int size);

	public List<RespMinOrder> getAllMin(int start, int size);

	public List<RespMinOrder> getAllOrderReturns(int start, int size);

}
