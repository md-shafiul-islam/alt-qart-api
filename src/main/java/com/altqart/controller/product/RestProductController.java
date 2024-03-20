package com.altqart.controller.product;

import java.util.HashMap;
import java.util.List;
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

import com.altqart.helper.services.HelperServices;
import com.altqart.req.model.ProductCatReq;
import com.altqart.req.model.ProductReq;
import com.altqart.resp.model.RespDetailProduct;
import com.altqart.resp.model.RespMinProduct;
import com.altqart.resp.model.RespProduct;
import com.altqart.services.ProductServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class RestProductController {

	@Autowired
	private ProductServices productServices;

	@Autowired
	private HelperServices helperServices;

	@GetMapping
	public ResponseEntity<?> getAllProduct(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size,
			@RequestParam(name = "type", defaultValue = "") String type) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Products not found");

		if (!helperServices.isNullOrEmpty(type)) {
			if (helperServices.isEqual("min", type)) {
				List<RespMinProduct> minProducts = productServices.getAllMinRespProduct(start, size);

				if (minProducts != null) {
					map.put("response", minProducts);
					map.put("status", true);
					map.put("message", minProducts.size() + " Product found");
				}

			}else if(helperServices.isEqual("rnd", type)) {
				List<RespMinProduct> minProducts = productServices.getRandomMinRespProduct(start, size);

				if (minProducts != null) {
					map.put("response", minProducts);
					map.put("status", true);
					map.put("message", minProducts.size() + " Product found");
				}
			}
		} else {

			List<RespProduct> products = productServices.getAllProducts(start, size);

			if (products != null) {
				map.put("response", products);
				map.put("status", true);
				map.put("message", products.size() + " Product found");
			}
		}

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> addProduct(@RequestBody ProductReq productReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Product Add failed");

		productServices.add(productReq, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> getUpdateProduct(@RequestBody ProductReq productReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Product update failed");

		productServices.update(productReq, map);

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getProduct(@PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", "Product not found");

		RespDetailProduct product = productServices.getDetailsProductByPublicId(id);

		if (product != null) {
			map.put("response", product);
			map.put("status", true);
			map.put("message", "Product found by ID");
		}

		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/category")
	public ResponseEntity<?> getProductSinglePage(@RequestBody ProductCatReq catReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", "Product not found By Category");

		productServices.getRespProductExceptCategory(catReq, map);

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/category/{value}")
	public ResponseEntity<?> getProductSinglePage(@PathVariable("value") String value) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", "Product not found By Category");

		productServices.getAllRespMinProductByCategory(value, map);

		return ResponseEntity.ok(map);
	}

}
