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
import com.altqart.resp.model.RespOrder;
import com.altqart.services.OrderServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
public class RestOrderController {

	@Autowired
	private OrderServices orderServices;

	@Autowired
	private HelperServices helperServices;

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
