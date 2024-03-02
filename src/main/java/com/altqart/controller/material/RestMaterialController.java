
package com.altqart.controller.material;

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

import com.altqart.req.model.MaterialReq;
import com.altqart.services.MaterialServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/materials")
public class RestMaterialController {

	@Autowired
	private MaterialServices materialServices;

	@GetMapping
	public ResponseEntity<?> getAllMaterial(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Materials not found");

		materialServices.getAllMaterial(map, start, size);

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> addMaterial(@RequestBody MaterialReq materialReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Materials add failed");
		materialServices.add(materialReq, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> updateMaterial(@RequestBody MaterialReq materialReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", "Update Materials failed");

		materialServices.update(materialReq, map);

		return ResponseEntity.ok(map);
	}

	@DeleteMapping
	public ResponseEntity<?> removeMaterial(@RequestBody MaterialReq materialReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Materials Remove failed");

		materialServices.remove(materialReq, map);

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getMaterialActin(@PathVariable("id") int id) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Materials not found by Id");

		materialServices.getOneById(id, map);

		return ResponseEntity.ok(map);
	}

}
