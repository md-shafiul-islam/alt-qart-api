
package com.altqart.controller.measurement;

import java.util.HashMap;
import java.util.List;
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

import com.altqart.req.model.MeasurementStandardReq;
import com.altqart.req.model.SizeReq;
import com.altqart.resp.model.RespMeasurementStandard;
import com.altqart.services.SizeServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/sizes")
public class RestSizeController {

	@Autowired
	private SizeServices sizeServices;

	@GetMapping
	public ResponseEntity<?> getAllSize(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Sizes not found");

		sizeServices.getAll(map, start, size);

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> addSize(@RequestBody SizeReq sizeReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Sizes add failed");
		sizeServices.add(sizeReq, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> updateSize(@RequestBody SizeReq sizeReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", "Update Sizes failed");

		sizeServices.update(sizeReq, map);

		return ResponseEntity.ok(map);
	}

	@DeleteMapping
	public ResponseEntity<?> removeSize(@RequestBody SizeReq sizeReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Sizes Remove failed");

		sizeServices.remove(sizeReq, map);

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getSizeActin(@PathVariable("id") int id) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Sizes not found by Id");

		sizeServices.getOneById(id, map);

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/standards")
	public ResponseEntity<?> getStandardActin() {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Sizes not found by Id");

		List<RespMeasurementStandard> standards = sizeServices.getAllRespMeasurementStandard();

		if (standards != null) {
			map.put("response", standards);
			map.put("status", true);
			map.put("message", standards.size() + " Standard's found");
		}

		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/standards")
	public ResponseEntity<?> getAddStandardActin(@RequestBody MeasurementStandardReq standard) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Sizes not found by Id");

		sizeServices.addMeasurementStandard(standard, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping(value = "/standards")
	public ResponseEntity<?> getUpdateStandardActin(@RequestBody MeasurementStandardReq standard) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Sizes not found by Id");

		sizeServices.updateMeasurementStandard(standard, map);

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/standards/{id}")
	public ResponseEntity<?> getStandardOneActin(@PathVariable("id") int id) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Sizes not found by Id");

		RespMeasurementStandard standard = sizeServices.getOneMeasurementStandard(id);

		if (standard != null) {
			map.put("response", standard);
			map.put("status", true);
			map.put("message", "Measurement Standard found by Id");
		}
		return ResponseEntity.ok(map);
	}

}
