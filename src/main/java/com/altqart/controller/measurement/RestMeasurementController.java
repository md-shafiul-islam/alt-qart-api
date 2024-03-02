
package com.altqart.controller.measurement;

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

import com.altqart.req.model.MeasurementReq;
import com.altqart.services.MeasurementServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/measurements")
public class RestMeasurementController {

	@Autowired
	private MeasurementServices measurementServices;

	@GetMapping
	public ResponseEntity<?> getAllMeasurement(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Measurements not found");

		measurementServices.getAll(map, start, size);

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> addMeasurement(@RequestBody MeasurementReq MeasurementReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Measurements add failed");
		measurementServices.add(MeasurementReq, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> updateMeasurement(@RequestBody MeasurementReq MeasurementReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", "Update Measurements failed");

		measurementServices.update(MeasurementReq, map);

		return ResponseEntity.ok(map);
	}

	@DeleteMapping
	public ResponseEntity<?> removeMeasurement(@RequestBody MeasurementReq MeasurementReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Measurements Remove failed");

		measurementServices.remove(MeasurementReq, map);

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getMeasurementActin(@PathVariable("id") int id) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Measurements not found by Id");

		measurementServices.getOneById(id, map);

		return ResponseEntity.ok(map);
	}

}
