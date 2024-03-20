
package com.altqart.controller.parcel;

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

import com.altqart.helper.services.HelperServices;
import com.altqart.req.model.ParcelReq;
import com.altqart.req.model.ParcelSendReq;
import com.altqart.services.ParcelClientService;
import com.altqart.services.ParcelProviderServices;
import com.altqart.services.ParcelServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/parcels")
public class RestParcelController {

	@Autowired
	private ParcelServices parcelServices;
	
	@Autowired
	private ParcelProviderServices parcelProviderServices;

	@Autowired
	private ParcelClientService parcelClientService;

	@Autowired
	private HelperServices helperServices;

	@GetMapping
	public ResponseEntity<?> getAllParcel(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size, @RequestParam(name= "type", defaultValue = "") String type) {

		Map<String, Object> map = new HashMap<>();


		if (helperServices.isEqual("pending", type)) {
			parcelServices.getAllPendingParcel(map, start, size);
		} else if (helperServices.isEqual("send", type)) {
			parcelServices.getAllSendParcel(map, start, size);
		} else if (helperServices.isEqual("delivered", type)) {
			parcelServices.getAllDeliveredParcel(map, start, size);
		} else if (helperServices.isEqual("delivery_failed", type)) {
			parcelServices.getAllDeliveryFailedParcel(map, start, size);
		} else if (helperServices.isEqual("payment_invoice", type)) {
			parcelServices.getAllPaymentInvoiceParcel(map, start, size);
		} else if (helperServices.isEqual("return", type)) {
			parcelServices.getAllReturnParcel(map, start, size);
		} else {
			parcelServices.getAllParcel(map, start, size, type);
		}

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getParcel(@PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		parcelServices.getParcelById(id);

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> addParcel(@RequestBody ParcelReq parcelReq) {

		Map<String, Object> map = new HashMap<>();

		parcelServices.addParcel(parcelReq, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> updateParcel(@RequestBody ParcelReq parcelReq) {

		Map<String, Object> map = new HashMap<>();

		parcelServices.updateParcel(parcelReq, map);

		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/send")
	public ResponseEntity<?> sendCreatedParcel(@RequestBody ParcelSendReq parcelSendReq) {

		Map<String, Object> map = new HashMap<>();
		
		log.info("Parcel Send Request "+parcelSendReq.getId());
		
		if (parcelSendReq != null) {
			if (parcelSendReq.isSend()) {
				parcelServices.sendParcel(parcelSendReq.getId(), map);
			}
		}

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/providers")
	public ResponseEntity<?> getAllProvider(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size) {

		Map<String, Object> map = new HashMap<>();

		parcelProviderServices.getAllProvider(map, start, size);

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/providers/{id}")
	public ResponseEntity<?> getParcelProvider(@PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		parcelProviderServices.getParcelProviderById(id);

		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/providers")
	public ResponseEntity<?> addParcelProvider(@RequestBody ParcelReq parcelReq) {

		Map<String, Object> map = new HashMap<>();

		return ResponseEntity.ok(map);
	}

	@PutMapping(value = "/providers")
	public ResponseEntity<?> updateParcelProvider(@RequestBody ParcelReq parcelReq) {

		Map<String, Object> map = new HashMap<>();

		parcelServices.updateParcel(parcelReq, map);

		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/test")
	public ResponseEntity<?> testParcelAction(@RequestParam(name = "token", defaultValue = "") String token) {

		Map<String, Object> map = new HashMap<>();

		map.put("status", false);
		map.put("message", "Parcel Test API ");
		map.put("INV: ", helperServices.getInvId());

		String baseUrl = "https://courier-api-sandbox.pathao.com";
		String uri = "/aladdin/api/v1/issue-token";

		map.put("response", parcelClientService.getCreateParcel(baseUrl, "/aladdin/api/v1/orders", ""));

		return ResponseEntity.ok(map);
	}

}
