
package com.altqart.controller.parcel;

import java.util.HashMap;
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

import com.altqart.req.model.ParcelReq;
import com.altqart.services.ParcelServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/parcels")
public class RestParcelController {

	@Autowired
	private ParcelServices parcelServices;

	@GetMapping
	public ResponseEntity<?> getAllParcel(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size) {

		Map<String, Object> map = new HashMap<>();

		parcelServices.getAllParcel(map, start, size);

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getParcel(@PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		parcelServices.getParcelById(id);

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> addParcel(@RequestBody ParcelReq parcelReq) {

		Map<String, Object> map = new HashMap<>();

		parcelServices.addParcel(parcelReq, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> updateParcel(@RequestBody ParcelReq parcelReq) {

		Map<String, Object> map = new HashMap<>();

		parcelServices.updateParcel(parcelReq, map);

		return ResponseEntity.ok(map);
	}

}
