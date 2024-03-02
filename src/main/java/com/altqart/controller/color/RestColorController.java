
package com.altqart.controller.color;

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

import com.altqart.req.model.ColorReq;
import com.altqart.services.ColorServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/colors")
public class RestColorController {

	@Autowired
	private ColorServices colorServices;

	@GetMapping
	public ResponseEntity<?> getAllColor(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Colors not found");

		colorServices.getAllColor(map, start, size);

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> addColor(@RequestBody ColorReq colorReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Colors add failed");
		colorServices.add(colorReq, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> updateColor(@RequestBody ColorReq colorReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", "Update Colors failed");

		colorServices.update(colorReq, map);

		return ResponseEntity.ok(map);
	}

	@DeleteMapping
	public ResponseEntity<?> removeColor(@RequestBody ColorReq colorReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Colors Remove failed");

		colorServices.remove(colorReq, map);

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getColorActin(@PathVariable("id") int id) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Colors not found by Id");

		colorServices.getOneById(id, map);

		return ResponseEntity.ok(map);
	}

}
