
package com.altqart.controller.parcel;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altqart.client.req.model.ParcelPriceReq;
import com.altqart.client.req.model.PathaoWebHookReq;
import com.altqart.helper.services.HelperServices;
import com.altqart.services.ParcelClientService;
import com.altqart.services.ParcelServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/pathao")
public class RestPathaoController {

	@Autowired
	private ParcelServices parcelServices;

	@Autowired
	private ParcelClientService parcelClientService;

	@Autowired
	private HelperServices helperServices;

	@PostMapping(value = "/test")
	public ResponseEntity<?> testPathaoAction(@RequestParam(name = "token", defaultValue = "") String token) {

		Map<String, Object> map = new HashMap<>();

		map.put("status", false);
		map.put("message", "Parcel Test API ");
		map.put("INV: ", helperServices.getInvId());

		String baseUrl = "https://courier-api-sandbox.pathao.com";
		String uri = "/aladdin/api/v1/issue-token";

		map.put("response", parcelClientService.getCreateParcel(baseUrl, "/aladdin/api/v1/orders", ""));

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/re-validate")
	public ResponseEntity<?> reValidatePathaoAction() {

		Map<String, Object> map = new HashMap<>();

		map.put("status", false);
		map.put("message", "Revalidate Failed");
		map.put("response", null);

		parcelClientService.getValidateToken(map);
		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/webhook")
	public ResponseEntity<?> pathaoWebhookAction(@RequestBody PathaoWebHookReq webHookReq,
			@RequestHeader("X-PATHAO-Signature") String key) {

		Map<String, Object> map = new HashMap<>();

		map.put("status", false);
		map.put("message", "Parcel Test webhook ");

		log.info("Web hook Headre Ket " + key);

		map.put("response", parcelServices.updatePathaoParcelViaWebHook(webHookReq, key));

		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/price")
	public ResponseEntity<?> getPathaoParcelPriceAction(@RequestBody ParcelPriceReq parcelPriceReq ) {

		Map<String, Object> map = new HashMap<>();
		
		map.put("status", false);
		map.put("message", "Revalidate Failed");
		map.put("response", null);

		parcelClientService.getParcelPrice(parcelPriceReq, map);
		return ResponseEntity.ok(map);
	}

}
