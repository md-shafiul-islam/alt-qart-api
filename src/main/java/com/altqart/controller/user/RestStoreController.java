package com.altqart.controller.user;

import java.util.HashMap;
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

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.req.model.AddressReq;
import com.altqart.req.model.NamePhoneNoReq;
import com.altqart.req.model.StoreReq;
import com.altqart.resp.model.RespStore;
import com.altqart.services.StoreServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/stores")
public class RestStoreController {

	@Autowired
	private StoreServices storeServices;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private HelperAuthenticationServices helperAuthenticationServices;

	@PostMapping
	public ResponseEntity<?> getAddStoreAction(@RequestBody StoreReq storeReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("status", false);
		map.put("response", null);
		map.put("message", "Store add failed");
		
		storeServices.getAddStore(storeReq, map);
		
		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> getUpdateStoreAction(@RequestBody StoreReq storeReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("status", false);
		map.put("response", null);
		map.put("message", "Store Update failed");
		
		storeServices.update(storeReq, map);
		return ResponseEntity.ok(map);
	}

	@GetMapping
	public ResponseEntity<?> getAllStore(@RequestParam(name = "size", defaultValue = "100") int size,
			@RequestParam(name = "start", defaultValue = "-1") int start) {

		Map<String, Object> map = new HashMap<>();

		map.put("status", false);
		map.put("response", null);
		map.put("message", "Get All Store failed");

		storeServices.getAllStore(start, size);
		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Map<String, Object>> getStore(@PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		map.put("status", false);
		map.put("response", null);
		map.put("message", "Get All Store failed");

		RespStore respStore = storeServices.getRespStoreById(id);
		
		if(respStore != null) {
			
			map.put("status", true);
			map.put("response", respStore);
			map.put("message", "Store found By ID");
		}
		
		return ResponseEntity.ok(map);

	}

	@GetMapping(value = "/{id}/phones")
	public ResponseEntity<Map<String, Object>> getStoreAddress(@PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();
		map.put("status", false);
		map.put("message", "Store Address Not found");
		map.put("response", null);

		storeServices.getAllStoreNamePhoneNo(id, map);

		return ResponseEntity.ok(map);

	}

	@PostMapping(value = "/{id}/phones")
	public ResponseEntity<Map<String, Object>> addStoreAddress(@PathVariable("id") String id,
			@RequestBody NamePhoneNoReq namePhoneNoReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("status", false);
		map.put("message", "Store Phone added failed");
		map.put("response", null);

		storeServices.addStoreNamePhoneNo(id, namePhoneNoReq, map);

		return ResponseEntity.ok(map);

	}

	@PutMapping(value = "/{id}/addresses")
	public ResponseEntity<Map<String, Object>> updateStoreAddress(@PathVariable("id") String id,
			@RequestBody NamePhoneNoReq namePhoneNoReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("status", false);
		map.put("message", "Store Address Not found");
		map.put("response", null);

		storeServices.updateStoreNamePhoneNo(id, namePhoneNoReq, map);

		return ResponseEntity.ok(map);

	}

}
