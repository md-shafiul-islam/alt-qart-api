
package com.altqart.controller.address;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.altqart.req.model.AddressReq;
import com.altqart.resp.model.SearchWordReq;
import com.altqart.services.AddressServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/addresses")
public class RestAddressController {

	@Autowired
	private AddressServices addressServices;

	@GetMapping
	public ResponseEntity<?> getAllAddress(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Address not found");

		addressServices.getAll(map, start, size);

		return ResponseEntity.ok(map);
	}

	@GetMapping("/stakeholder")
	public ResponseEntity<?> getAllStakeholderAddress() {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Address not found");

		addressServices.getStakeholderAddress(map);

		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/search")
	public ResponseEntity<?> getAddressSearch(@RequestBody SearchWordReq words) {

		log.info("Get Address Search " + words);

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Search Address not found");

		addressServices.getAllSearchSuggestion(words, map);

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> addAddress(@RequestBody AddressReq addressReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("response", null);
		map.put("status", false);
		map.put("message", " Address Add failed");

		addressServices.add(addressReq, map);

		return ResponseEntity.ok(map);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> removeAddress(@RequestBody AddressReq addressReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Address remove failed");

		addressServices.remove(addressReq, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> updateAddress(@RequestBody AddressReq addressReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Address updated");

		addressServices.update(addressReq, map);

		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/test")
	public ResponseEntity<?> testAddress() {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", "Test Address");

		final String url = "https://courier-api-sandbox.pathao.com/countries/1/city-list";

		String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjFkY2EwNWFmYzcxMTg1MWE0N2I1NmI5ZGI1ODhkNWE1MzM4OWRmZjhiY2U2OGYzMmY0OTYwODg3YzM0OGFlNDY5ZjVhNDMwMWNhZTFiMDNkIn0.eyJhdWQiOiIyNjciLCJqdGkiOiIxZGNhMDVhZmM3MTE4NTFhNDdiNTZiOWRiNTg4ZDVhNTMzODlkZmY4YmNlNjhmMzJmNDk2MDg4N2MzNDhhZTQ2OWY1YTQzMDFjYWUxYjAzZCIsImlhdCI6MTcwNjE5MDk0OSwibmJmIjoxNzA2MTkwOTQ5LCJleHAiOjE3MDY2MjI5NDksInN1YiI6IjM1MiIsInNjb3BlcyI6W119.rsWVsx7fwu_Tf9tI9ol1Pxm-FnZ9pCAhMaLP75MALlykXtUAeJ5NRk6TVXcJ_Lir5219kOt5OnTj4fisbZT82JryphRwXa6AixkZ51tMxS4P2L8ORpZfavuHbhNV404rfxjeMfDeBoziTldtm2p3R_yAUdAoaYVtMVl7h9nZ5goAnepR12VzltyMmA8numCW5Cu96ISS5OZvJVlF9J7XbI0KJuqwa5j-Far6vdF1j2xoKKAwcsEnz5S4WbrXU036EyCjKy_oyKq-Ged4_rg8f7nc1ABmAyPnV3N8_GJnLCYAH0A6I-pEx8hW8oybyEuH2F0NPvI9vtzuqTTfnlx85IDiLKwT942f_7yxdql3Bwpb-yxqX6iQJy7FTLGoLDdSAXsamcGUniRJLEdFGGkBYVgAaWbYS07OvR6032bnzD0Wq9On6R0qUeBrhA_vS1gTJIQdRqzX6G7U5xYRzHaSuOqT7_z4Z0yX4gz_GZDA-l5D5Su74zKu24SPpCZ5i-Q_vAEWubK6P-ju2E6qymlGHE3rbXDqtYOR73D7U_LzQ_alVTafYii0TmeiIHYVlT_niLXEETAE7Rk_AHGxVKQ_t1ymoYPOqMmSbgHgoe6jVnludKFaSXvShCJtQuQbN-Xrv2irZ2V1EuNQumGFz79oN_SQv6PM8eyHKp7fjZyw7Kk";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		Object reponse = restTemplate.getForObject(url, Object.class, headers);

		return ResponseEntity.ok(reponse);
	}

}
