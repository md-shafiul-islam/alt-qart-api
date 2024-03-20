package com.altqart.controller.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altqart.helper.services.HelperServices;
import com.altqart.req.model.OrderPlaceReq;
import com.altqart.req.model.OrderUpateReq;
import com.altqart.resp.model.RespMinOrder;
import com.altqart.resp.model.RespOrder;
import com.altqart.services.OrderServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/orders")
public class RestAdminOrderController {

	@Autowired
	private OrderServices orderServices;

	@Autowired
	private HelperServices helperServices;

	@GetMapping
	public ResponseEntity<?> getAllOrders(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size,
			@RequestParam(name = "type", defaultValue = "default") String type) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Orders not found");
		
		log.info("getAllOrders, "+ type);

		List<RespMinOrder> orders = null;
		if (helperServices.isEqualAndFirstOneIsNotNull(type, "pending")) {

			orders = orderServices.getAllOrderPending(start, size);
			if (orders != null) {
				map.put("message", orders.size() + " Pending Orders found");
			}

		} else if (helperServices.isEqualAndFirstOneIsNotNull(type, "readytoship")) {

			orders = orderServices.getAllProcessesOrder(start, size);
			if (orders != null) {
				map.put("message", orders.size() + " Rady to Ship Orders found");
			}
		} else if (helperServices.isEqualAndFirstOneIsNotNull(type, "shipped")) {

			orders = orderServices.getAllOrderShipped(start, size);
			if (orders != null) {
				map.put("message", orders.size() + " Shipped Orders found");
			}

		} else if (helperServices.isEqualAndFirstOneIsNotNull(type, "delivered")) {

			orders = orderServices.getAllOrderDelivered(start, size);
			if (orders != null) {
				map.put("message", orders.size() + " Delivered Orders found");
			}

		} else if (helperServices.isEqualAndFirstOneIsNotNull(type, "cancel")) {

			orders = orderServices.getAllOrderCanceled(start, size);
			if (orders != null) {
				map.put("message", orders.size() + " Canceled Orders found");
			}

		} else if (helperServices.isEqualAndFirstOneIsNotNull(type, "failed_delivery")) {

			orders = orderServices.getAllOrderFailedDelivery(start, size);
			if (orders != null) {
				map.put("message", orders.size() + "Failed Delivery Orders found");
			}

		} else if (helperServices.isEqualAndFirstOneIsNotNull(type, "return")) {
			orders = orderServices.getAllOrderReturns(start, size);
			if (orders != null) {
				map.put("message", orders.size() + "Return Orders found");
			}
		} else {
			orders = orderServices.getAllMin(start, size);

			if (orders != null) {
				map.put("message", orders.size() + " Orders found");
			}
		}

		if (orders != null) {
			map.put("response", orders);
			map.put("status", true);
		}

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getOrder(@PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Order not found by id");

		RespOrder order = orderServices.getOrderDetailsByPublicId(id);

		if (order != null) {
			map.put("response", order);
			map.put("status", true);
			map.put("message", "Order found by id");
		}

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> getUpdateOrder(@RequestBody OrderUpateReq orderUpdateReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);

		map.put("message", "Order Update failed");

		if (orderUpdateReq != null) {


			if (helperServices.isEqualAndFirstOneIsNotNull(orderUpdateReq.getStatus(), "readytoship")) {

				map = orderServices.approveOrder(orderUpdateReq.getId());

			} else if (helperServices.isEqualAndFirstOneIsNotNull(orderUpdateReq.getStatus(), "shipped")) {

				orderServices.updateOrderStatus(orderUpdateReq.getId(), orderUpdateReq.getStatus(), map);

			} else if (helperServices.isEqualAndFirstOneIsNotNull(orderUpdateReq.getStatus(), "delivered")) {

				orderServices.updateOrderStatus(orderUpdateReq.getId(), orderUpdateReq.getStatus(), map);

			} else if (helperServices.isEqualAndFirstOneIsNotNull(orderUpdateReq.getStatus(), "cancel")) {

				orderServices.updateOrderStatus(orderUpdateReq.getId(), orderUpdateReq.getStatus(), map);

			} else if (helperServices.isEqualAndFirstOneIsNotNull(orderUpdateReq.getStatus(), "failed_delivery")) {

				orderServices.getOrderFaileldDeliveryApprove(orderUpdateReq.getId(), map);

			} else if (helperServices.isEqualAndFirstOneIsNotNull(orderUpdateReq.getStatus(), "sale_return")) {

				orderServices.getSaleReturnApprove(orderUpdateReq.getId(), map); // (orderUpdateReq.getId(),
																					// orderUpdateReq.getStatus(), map);
			}
		}

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> getPlaceOrder(@RequestBody OrderPlaceReq orderPlaceReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Order place failed");

		orderServices.getOrderPlace(orderPlaceReq, map);

		return ResponseEntity.ok(map);
	}

}
