
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

import com.altqart.req.model.AreaReq;
import com.altqart.req.model.AreasReq;
import com.altqart.req.model.AreasZReq;
import com.altqart.services.AreasServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/areas")
public class RestAreaController {

	@Autowired
	private AreasServices areasServices;

	@GetMapping
	public ResponseEntity<?> getAllAreas(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Areas not found");

		areasServices.getAll(map, start, size);

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> addAreas(@RequestBody AreaReq areaReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Areas Add failed");

		areasServices.add(areaReq, map);

		return ResponseEntity.ok(map);
	}
	
	@GetMapping(value = "/zone/{id}")
	public ResponseEntity<?> getAllAreaByZone(@PathVariable("id") int id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Get Areas By Zone failed");

		areasServices.getAllAreaByZone(id, map);

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getArea(@PathVariable("id") int id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Areas remove failed");

		areasServices.getOne(id, map);

		return ResponseEntity.ok(map);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> removeAreas(@RequestBody AreaReq areaReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Areas remove failed");

		areasServices.remove(areaReq, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> updateAreas(@RequestBody AreaReq areaReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Areas updated");

		areasServices.update(areaReq, map);

		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/all")
	public ResponseEntity<?> addAllAreas(@RequestBody AreasReq areasReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Areas updated");

		areasServices.addAll(areasReq, map);

		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/zall")
	public ResponseEntity<?> addAllZAreas(@RequestBody AreasZReq areasReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Areas Z All");

		
		areasServices.addZAll(areasReq, map);

		return ResponseEntity.ok(map);
	}

}
