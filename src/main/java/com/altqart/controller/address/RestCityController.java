
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

import com.altqart.req.model.CitiesReq;
import com.altqart.req.model.CityReq;
import com.altqart.services.CitiyServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/cities")
public class RestCityController {

	@Autowired
	private CitiyServices citiyServices;

	@GetMapping
	public ResponseEntity<?> getAllCity(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size,
			@RequestParam(value = "type", defaultValue = "0") int type) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " City not found");
		
	
		citiyServices.getAll(map, start, size, type);

		return ResponseEntity.ok(map);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCity(@PathVariable("id") int id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " City not found");

		citiyServices.getOne(id, map);

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> addCity(@RequestBody CityReq cityReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " City Add failed");

		citiyServices.add(cityReq, map);

		return ResponseEntity.ok(map);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> removeCity(@RequestBody CityReq cityReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " City remove failed");

		citiyServices.remove(cityReq, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> updateCity(@RequestBody CityReq cityReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " City updated");

		citiyServices.update(cityReq, map);

		return ResponseEntity.ok(map);
	}

	@PostMapping("/all")
	public ResponseEntity<?> addAllCity(@RequestBody CitiesReq cityReqs) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " City updated");

		citiyServices.addAll(cityReqs, map);

		return ResponseEntity.ok(map);
	}

}
