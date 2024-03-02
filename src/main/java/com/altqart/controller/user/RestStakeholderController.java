package com.altqart.controller.user;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altqart.exception.InvalidLoginResponse;
import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.model.Access;
import com.altqart.model.Credential;
import com.altqart.model.Stakeholder;
import com.altqart.model.User;
import com.altqart.req.model.AddressReq;
import com.altqart.req.model.EsUserActionReq;
import com.altqart.req.model.ReqLoginData;
import com.altqart.req.model.RestPassowrdReq;
import com.altqart.req.model.StakeholderReq;
import com.altqart.req.model.UserReq;
import com.altqart.resp.model.JWTLoginSucessReponse;
import com.altqart.resp.model.RespAccessUser;
import com.altqart.resp.model.RespUser;
import com.altqart.security.config.JwtTokenProvider;
import com.altqart.security.config.SecurityConstants;
import com.altqart.services.StakeholderServices;
import com.altqart.services.UserServices;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/stakeholders")
public class RestStakeholderController {

	@Autowired
	private StakeholderServices stakeholderServices;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private HelperAuthenticationServices helperAuthenticationServices;

	@PostMapping
	public ResponseEntity<?> getAddStakeholderAction(@RequestBody StakeholderReq stakeholderReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("status", false);
		map.put("response", null);
		map.put("message", "Stakeholder add failed");

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> getUpdateStakeholderAction(@RequestBody StakeholderReq stakeholderReq) {

		Map<String, Object> map = new HashMap<>();

		map.put("status", false);
		map.put("response", null);
		map.put("message", "Stakeholder Update failed");

		return ResponseEntity.ok(map);
	}

	@GetMapping
	public ResponseEntity<?> getAllStakeholder(@RequestParam(name = "size", defaultValue = "100") int size,
			@RequestParam(name = "start", defaultValue = "-1") int start) {

		Map<String, Object> map = new HashMap<>();

		map.put("status", false);
		map.put("response", null);
		map.put("message", "Get All Stakeholder failed");

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Map<String, Object>> getStakeholder(@PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		map.put("status", false);
		map.put("response", null);
		map.put("message", "Get All Stakeholder failed");

		return ResponseEntity.ok(map);

	}

	@GetMapping(value = "/{id}/addresses")
	public ResponseEntity<Map<String, Object>> getStakeholderAddress(@PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();
		map.put("status", false);
		map.put("message", "Stakeholder Address Not found");
		map.put("response", null);

		stakeholderServices.getAllStakeholderAddress(map);

		return ResponseEntity.ok(map);

	}

	@PostMapping(value = "/{id}/addresses")
	public ResponseEntity<Map<String, Object>> addStakeholderAddress(@PathVariable("id") String id,
			@RequestBody AddressReq addressReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("status", false);
		map.put("message", "Stakeholder Address Not found");
		map.put("response", null);

		stakeholderServices.getAddStakeholderAddress(map, addressReq);

		return ResponseEntity.ok(map);

	}

	@PutMapping(value = "/{id}/addresses")
	public ResponseEntity<Map<String, Object>> updateStakeholderAddress(@PathVariable("id") String id,
			@RequestBody AddressReq addressReq) {

		Map<String, Object> map = new HashMap<>();
		map.put("status", false);
		map.put("message", "Stakeholder Address Not found");
		map.put("response", null);

		stakeholderServices.updateStakeholderAddress(map, addressReq);

		return ResponseEntity.ok(map);

	}

}
