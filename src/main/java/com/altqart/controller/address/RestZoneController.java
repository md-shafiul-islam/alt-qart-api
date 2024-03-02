
package com.altqart.controller.address;

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

import com.altqart.req.model.ZoneReq;
import com.altqart.req.model.ZonesCityReq;
import com.altqart.req.model.ZonesReq;
import com.altqart.services.ZoneServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/zones")
public class RestZoneController {

	@Autowired
	private ZoneServices zoneServices;

	@GetMapping
	public ResponseEntity<?> getAllZone(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Zone not found");

		zoneServices.getAll(map, start, size);

		return ResponseEntity.ok(map);
	}
	
	@GetMapping(value = "/city/{id}")
	public ResponseEntity<?> getAllZoneByCity(@PathVariable("id") int id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Zone not found");

		zoneServices.getAllByCity(id, map);

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> addZone(@RequestBody ZoneReq zoneReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", "Zone Add failed");

		zoneServices.add(zoneReq, map);

		return ResponseEntity.ok(map);
	}

	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getZone(@PathVariable("id") int id) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", "Zone Add failed");

		zoneServices.getOne(id, map);

		return ResponseEntity.ok(map);
	}

	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> removeZone(@RequestBody ZoneReq zoneReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Zone remove failed");

		zoneServices.remove(zoneReq, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> updateAddress(@RequestBody ZoneReq zoneReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Zone updated");

		zoneServices.update(zoneReq, map);

		return ResponseEntity.ok(map);
	}
	
	@PostMapping(value = "/all")
	public ResponseEntity<?> addAllZone(@RequestBody ZonesReq zonesReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Add All Zone");

		zoneServices.addAll(zonesReq, map);

		return ResponseEntity.ok(map);
	}
	
	@PostMapping(value = "/call")
	public ResponseEntity<?> addCityAllZone(@RequestBody ZonesCityReq zonesReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Add All Zone");

		zoneServices.addCityZoneAll(zonesReq, map);

		return ResponseEntity.ok(map);
	}
	
	

}
