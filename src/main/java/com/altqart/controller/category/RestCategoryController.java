
package com.altqart.controller.category;

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

import com.altqart.helper.services.HelperServices;
import com.altqart.req.model.CategoryReq;
import com.altqart.resp.model.RespCategory;
import com.altqart.services.CategoryServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
public class RestCategoryController {

	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private HelperServices helperServices;

	@GetMapping
	public ResponseEntity<?> getAllCategory(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Categories not found");

		List<RespCategory> categories = categoryServices.getAllRespCategory();

		if (categories != null) {
			map.put("response", categories);
			map.put("status", true);
			map.put("message", categories.size() + " Category found");

		}

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> addCategoryAction(@RequestBody CategoryReq categoryReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Category add failed");

		categoryServices.addCategory(categoryReq, map);

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getCategory(@PathVariable("id") int id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Category not found By Id");

		RespCategory category = categoryServices.getRespCategoryById(id);

		if (category != null) {
			map.put("response", category);
			map.put("status", true);
			map.put("message", "Category found By ID");

		}
		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/query")
	public ResponseEntity<?> getCategoryByQuery(@RequestParam(name = "value", defaultValue = "") String value) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Sub Categories not found By Query");

		RespCategory category = null;

		category = categoryServices.getRespCategoryByValue(value);

		if (category != null) {
			map.put("response", category);
			map.put("status", true);
			map.put("message", "Category found By ID");

		}
		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/sub-cats/{id}")
	public ResponseEntity<?> getAllSubCategoryByParen(@PathVariable("id") int id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Sub Categories not found By Parent");

		List<RespCategory> categories = categoryServices.getAllSubRespCategoryById(id);

		if (categories != null) {
			map.put("response", categories);
			map.put("status", true);
			map.put("message", categories.size() + " Sub Categories found By Parent");

		}
		return ResponseEntity.ok(map);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> removeCartItem(@PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Category delete failed");

		categoryServices.removeCategory(id, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> updateCartItem(@RequestBody CategoryReq categoryReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Category Update failed");

		categoryServices.updateCategory(categoryReq, map);

		return ResponseEntity.ok(map);
	}

}
