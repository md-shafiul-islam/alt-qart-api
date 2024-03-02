
package com.altqart.controller.courier;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altqart.req.model.CourierReq;
import com.altqart.services.CourierServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/Couriers")
public class RestCourierController {

	@Autowired
	private CourierServices courierServices;

	@GetMapping
	public ResponseEntity<?> getAllCourier(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size) {

		Map<String, Object> map = new HashMap<>();

		courierServices.getAllCourier(map, start, size);

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> addCourier(@RequestBody CourierReq Courier) {

		Map<String, Object> map = new HashMap<>();

		courierServices.addCourier(Courier, map);

		return ResponseEntity.ok(map);
	}

	@DeleteMapping(value = "/{id}/")
	public ResponseEntity<?> removeCourierItem(@PathVariable("id") String id, @RequestBody CourierReq courierReq) {

		Map<String, Object> map = new HashMap<>();

		courierServices.removeCourier(courierReq, map);

		return ResponseEntity.ok(map);
	}

	
	@PutMapping
	public ResponseEntity<?> updateCourier(@RequestBody CourierReq Courier) {

		Map<String, Object> map = new HashMap<>();

		courierServices.updateCourier(Courier, map);

		return ResponseEntity.ok(map);
	}

}
