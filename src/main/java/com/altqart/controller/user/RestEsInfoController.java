package com.altqart.controller.user;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.model.Access;
import com.altqart.model.User;
import com.altqart.resp.model.EsInitInf;
import com.altqart.resp.model.JWTLoginSucessReponse;
import com.altqart.resp.model.RespUser;
import com.altqart.security.config.JwtTokenProvider;
import com.altqart.security.config.SecurityConstants;
import com.altqart.services.UserServices;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/esinfos")
public class RestEsInfoController {

	@Autowired
	private UserServices userServices;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private HelperAuthenticationServices helperAuthenticationServices;

	private List<RespUser> updateableUserList;

	private List<RespUser> restRejectedUsers;

	private Access userAccess;

	@PostMapping
	public ResponseEntity<?> getPlasholderUserAction(@RequestBody String empty) {

		Map<String, Object> map = new HashMap<>();

		map.put("status", false);
		map.put("response", null);
		map.put("message", "Initial failed");

		userServices.createEsPlaceholderUser(empty, map);

		return ResponseEntity.ok(map);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getEsToken(@PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		map.put("status", false);
		map.put("response", null);
		map.put("message", "Geting Initial token failed");

		userServices.getEsTokenBySequence(id, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping("/token")
	public ResponseEntity<?> getToken(@RequestBody EsInitInf esInitInf, BindingResult bindingResult,
			HttpServletRequest httpServletRequest) {
		
		log.info("/Token getToken "+esInitInf);

		
		String jwt = null;
		if (esInitInf != null) {
			User user = userServices.getUserByEsUserAuth(esInitInf.getId());
			
			if(user == null) {
				user = userServices.getUserByPlsToken(esInitInf.getEsInf());
			}
			
			if (user != null) {

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
						null, Collections.emptyList());

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
				jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
			}
			
		}

		return ResponseEntity.ok(new JWTLoginSucessReponse(true, jwt));
	}

}
