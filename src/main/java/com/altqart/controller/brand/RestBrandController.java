
package com.altqart.controller.brand;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altqart.req.model.ReqBrand;
import com.altqart.services.BrandServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/brands")
public class RestBrandController {

	@Autowired
	private BrandServices brandServices;

	@GetMapping
	public ResponseEntity<?> getAllBrands(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Brand not found");

		brandServices.getAllRespBrand(map, start, size);

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> addBrand(@RequestBody ReqBrand brandReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Brand Add failed");

		brandServices.addBrand(brandReq, map);

		return ResponseEntity.ok(map);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> removeBrand(@RequestBody ReqBrand brandReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Brand remove failed");

		brandServices.removeBrand(brandReq, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> updateBrand(@RequestBody ReqBrand brandReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Brand updated");

		brandServices.update(brandReq, map);

		return ResponseEntity.ok(map);
	}

}
