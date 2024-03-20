package com.altqart.controller.product;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altqart.helper.services.HelperServices;
import com.altqart.req.model.CouponActionReq;
import com.altqart.req.model.CouponReq;
import com.altqart.services.CouponServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/coupons")
public class RestCouponController {

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private CouponServices couponServices;

	@GetMapping
	public ResponseEntity<?> getAllCoupon(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size,
			@RequestParam(name = "type", defaultValue = "") int type) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Coupons not found");

		couponServices.getAllCouponByType(start, size, type, map);

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getOneCoupon(@PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Coupons not found");

		if (helperServices.isValidAndLenghtCheck(id, 32)) {
			couponServices.getOne(id, map);
		}

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/code")
	public ResponseEntity<?> getOneCouponCode(@RequestParam("code") String code) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Coupon not found by code");

		couponServices.getRespCouponByCode(code, map);

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> getAddCoupon(@RequestBody CouponReq couponReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Coupons not found");

		couponServices.add(couponReq, map);

		return ResponseEntity.ok(map);
	}

	@PostMapping("/action")
	public ResponseEntity<?> getCouponAction(@RequestBody CouponActionReq actionReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Coupons not found");

		couponServices.action(actionReq, map);

		return ResponseEntity.ok(map);
	}

}
