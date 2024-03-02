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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.altqart.exception.InvalidLoginResponse;
import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.model.Access;
import com.altqart.model.Credential;
import com.altqart.model.User;
import com.altqart.req.model.EsUserActionReq;
import com.altqart.req.model.ReqLoginData;
import com.altqart.req.model.RestPassowrdReq;
import com.altqart.req.model.UserReq;
import com.altqart.resp.model.JWTLoginSucessReponse;
import com.altqart.resp.model.RespAccessUser;
import com.altqart.resp.model.RespUser;
import com.altqart.security.config.JwtTokenProvider;
import com.altqart.security.config.SecurityConstants;
import com.altqart.services.UserServices;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class RestUserController {

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

	@PostMapping(value = "/sign-up")
	public ResponseEntity<?> getAddUserAction(@RequestBody UserReq user) {

		Map<String, Object> map = new HashMap<>();

		map.put("status", false);
		map.put("response", null);
		map.put("message", "User SignUp Failed");

		userServices.signUpUser(user, map);

		return ResponseEntity.ok(map);
	}

	@GetMapping("/unicid")
	public ResponseEntity<?> getUnicIdTest() {

		String unicId = "";

		unicId = helperServices.getUnicId();

		log.info("User Generated Unic ID: " + unicId);
		unicId = unicId + "   " + Integer.toString(unicId.length());
		return ResponseEntity.ok(unicId);
	}

	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllUsers() {

		userAccess = helperAuthenticationServices.getAccessByMenuName("user");

		Map<String, Object> map = new HashMap<>();
		map.put("status", false);
		map.put("message", "User(s) Not found");
		map.put("response", null);

		// TODO: Remove this Code
		// Remove Code Start
		List<RespUser> users = userServices.getAllRespUserOnly();
		if (users != null) {
			map.put("status", true);
			map.put("message", users.size() + " User(s) found");
			map.put("response", users);
		}
		// Remove Code End
		if (userAccess != null) {

//			if (userAccess.getNoAccess() == 0) {
//				List<RespEsUser> users = userServices.getAllRespUserOnly();
//
//				if (users != null) {
//					map.put("status", true);
//					map.put("message", users.size() + " User(s) found");
//					map.put("response", users);
//				}
//			}
		}

		return ResponseEntity.ok(map);

	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Map<String, Object>> getUser(@PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();
		map.put("status", false);
		map.put("message", "User(s) Not found");
		map.put("response", null);

		if (helperServices.isValidAndLenghtCheck(id, 30)) {
			userAccess = helperAuthenticationServices.getAccessByMenuName("user");
			RespUser respUser = null;
			if (helperAuthenticationServices.isOwn(id)) {
				respUser = userServices.getUserDetailsByPublicID(id);
			} else {
				if (userAccess != null) {
					if (userAccess.isEdit() || userAccess.isApprove() || userAccess.isAdd()
							|| userAccess.isUpdateApproval() || userAccess.isAll()) {

						respUser = userServices.getUserDetailsByPublicID(id);

					}
				}
			}

			if (respUser != null) {
				map.put("status", true);
				map.put("message", "User found");
				map.put("response", respUser);
			}

		}

		return ResponseEntity.ok(map);

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserLoginAction(@RequestBody ReqLoginData loginData, BindingResult bindingResult,
			HttpServletRequest httpServletRequest) {

		log.info("Run User Controller User Login");
		// ResponseEntity<?>
		String jwt = null;
		if (loginData != null) {
			User user = userServices.getUserByUsername(loginData.getUsername());

			if (user == null) {
				user = userServices.getUserByPhoneNo(loginData.getUsername());
			}

			if (user == null) {
				user = userServices.getUserByEmail(loginData.getUsername());
			}

			if (user != null) {

				if (passwordEncoder.matches(loginData.getPassword(), user.getPassword())) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
							null, Collections.emptyList());

					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
					jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
				} else {
					return ResponseEntity.ok(new InvalidLoginResponse());
				}
			} else {

				Authentication authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginData.getUsername(), loginData.getPassword()));

				SecurityContextHolder.getContext().setAuthentication(authentication);

				jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
			}

			if (user != null) {
				if (!user.isAccountNonLocked() || !user.isEnabled()) {
					InvalidLoginResponse loginResponse = new InvalidLoginResponse();
					loginResponse.setUsername("");
					loginResponse.setPassword("");

					if (!user.isAccountNonLocked()) {
						loginResponse.setMessage("Acoount is temporary locked, Please contact administrator!");
					}

					if (!user.isEnabled()) {
						loginResponse.setMessage("Acoount is temporary Disable, Please contact administrator!");
					}

					return ResponseEntity.ok(loginResponse);
				}
			}
			
			Map<String, Object> map = new HashMap<>();
		}

		return ResponseEntity.ok(new JWTLoginSucessReponse(true, jwt));

	}

	@RequestMapping(value = "/edit-users", method = RequestMethod.GET)
	public ResponseEntity<List<?>> getEditableUsers(Principal principal, HttpServletRequest request) {

		List<String> msgList = new ArrayList<>();

		// Set Access controls Start
		Map<String, RespAccessUser> accessMap = helperServices.getAccessMapByPrincipal(principal);
		RespAccessUser accessUser = null;

		if (accessMap != null) {

			accessUser = accessMap.get("user");
			if (accessUser == null) {
				msgList.add(new String("Login And try Again "));
				return ResponseEntity.ok(msgList);
			} else {

				if (accessUser.getEdit() == 1 || accessUser.getAll() == 1) {

				} else {
					msgList.add(new String("You cann't Access this options Please contact Administartor "));
					return ResponseEntity.ok(msgList);
				}
			}
		}

		// Set Access controls End

		List<String> errors = new ArrayList<>();

		if (updateableUserList != null) {

			return ResponseEntity.ok(updateableUserList);
		}

		return ResponseEntity.accepted().body(errors);
	}

	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ResponseEntity<List<?>> getAllRejectedUsers(Principal principal, HttpServletRequest request) {

		List<String> msgList = new ArrayList<>();

		// Set Access controls Start
		Map<String, RespAccessUser> accessMap = helperServices.getAccessMapByPrincipal(principal);
		RespAccessUser accessUser = null;

		if (accessMap != null) {

			accessUser = accessMap.get("user");
			if (accessUser == null) {
				msgList.add(new String("Login And try Again "));
				return ResponseEntity.ok(msgList);
			} else {

				if (accessUser.getAdd() == 1 || accessUser.getAll() == 1) {

				} else {
					msgList.add(new String("You cann't Access this options Please contact Administartor "));
					return ResponseEntity.ok(msgList);
				}
			}
		}

		// Set Access controls End

		List<String> msg = new ArrayList<>();

		if (restRejectedUsers != null) {

			return ResponseEntity.ok(restRejectedUsers);
		}

		return ResponseEntity.accepted().body(msg);
	}

	@PostMapping(value = "/{id}/change-pass")
	public ResponseEntity<Map<String, Object>> getUpdateUserPassword(@PathVariable("id") String id,
			@RequestBody RestPassowrdReq resPassword) {

		Map<String, Object> map = new HashMap<>();
		map.put("status", false);
		map.put("message", "User password rest faild");
		map.put("response", null);

		boolean status = false;

		if (helperServices.isValidAndLenghtCheck(id, 30)) {
			User user = helperAuthenticationServices.getCurrentUser();
			if (resPassword != null) {

				if (helperAuthenticationServices.isOwn(id)) {

					if (passwordEncoder.matches(resPassword.getOldPass(), user.getPassword())) {
						log.info("New Password " + resPassword.getNewPassword() + " CP "
								+ resPassword.getConfPassword());

						if (helperServices.isEqual(resPassword.getNewPassword(), resPassword.getConfPassword())) {
							Credential credential = new Credential();
							credential.setDate(new Date());
							credential.setPassword(resPassword.getNewPassword());
							credential.setStatus(1);
							credential.setUser(user);
							status = userServices.updatePassword(credential);
						} else {
							map.put("message", "Confirm  password is not match !!");
						}
					} else {

						map.put("message", "Old password is not correct");
					}

				} else {
					map.put("message", "You can't change Or Update another user password");
				}
			} else {
				map.put("message", "Password update information is not correct");
			}

			if (status) {
				map.put("status", true);
				map.put("message", "User password Updated");
				map.put("response", null);
			}

		}

		return ResponseEntity.ok(map);

	}

	@PostMapping(value = "/{id}/locked")
	public ResponseEntity<Map<String, Object>> getUserUpdateLockedAction(@PathVariable("id") String id,
			@RequestBody EsUserActionReq esUserAction) {
		String msg = "";

		if (esUserAction.getLocked() == 1) {
			msg = "User Locked ";
		} else {
			msg = "User Unlocked ";
		}
		Map<String, Object> map = new HashMap<>();
		map.put("status", false);
		map.put("message", msg + "failed");
		map.put("response", null);

		// TODO: update accessStatus by role
		boolean accessStatus = true, status = false;

		if (helperServices.isValidAndLenghtCheck(id, 30)) {
			Access userAccess = helperAuthenticationServices.getAccessByMenuName("user");
			User user = userServices.getUserByPublicID(esUserAction.getId());
			if (userAccess != null) {
				if (userAccess.isAll() || userAccess.isApprove() || userAccess.isUpdateApproval()) {
					accessStatus = true;
				}
			}

			if (user.isAccountNonLocked() && esUserAction.getLocked() == 0) {
				map.put("message", "User already Unlocked.");
			} else if (!user.isAccountNonLocked() && esUserAction.getLocked() == 1) {
				map.put("message", "User already Locked.");
			} else {
				if (esUserAction != null && accessStatus) {
					status = userServices.updateUserLocked(esUserAction.getLocked(), user);
				} else {
					map.put("message", "Please, Contact administrator");
				}
			}

		}

		if (status) {
			map.put("status", true);
			map.put("message", msg + "successfully");
			map.put("response", null);
		}

		return ResponseEntity.ok(map);

	}

	@PostMapping(value = "/{id}/enabled")
	public ResponseEntity<Map<String, Object>> getUserEnabledAction(@PathVariable("id") String id,
			@RequestBody EsUserActionReq esUserAction) {

		Map<String, Object> map = new HashMap<>();
		map.put("status", false);

		map.put("response", null);

		String msg = "";
		if (esUserAction.getEnabled() == 1) {
			msg = "User Enabled ";
		} else {
			msg = "User Disabled ";
		}
		map.put("message", msg + "faild");

		// TODO: update accessStatus by role
		boolean accessStatus = true, status = false;

		if (helperServices.isValidAndLenghtCheck(id, 30) && helperServices.isEqual(id, esUserAction.getId())) {
			Access userAccess = helperAuthenticationServices.getAccessByMenuName("user");
			User user = userServices.getUserByPublicID(esUserAction.getId());
			if (userAccess != null) {
				if (userAccess.isAll() || userAccess.isApprove() || userAccess.isUpdateApproval()) {
					accessStatus = true;
				}
			}

			if (esUserAction != null && accessStatus) {
				if (user.isEnabled() && esUserAction.getEnabled() == 1) {
					map.put("message", "User already enabled.");
				} else if (!user.isEnabled() && esUserAction.getEnabled() == 0) {
					map.put("message", "User already disabled.");
				} else {
					status = userServices.updateUserEnabled(esUserAction.getEnabled(), user);
				}

			} else {
				map.put("message", "Please, Contact administrator");
			}

			if (status) {
				map.put("status", true);
				map.put("message", msg + "successfully");
				map.put("response", null);
			}

		}

		return ResponseEntity.ok(map);

	}

	@PostMapping(value = "/{id}/check-pass")
	public ResponseEntity<Map<String, Object>> getCheckUserPassword(@PathVariable("id") String id,
			@RequestBody RestPassowrdReq resPassword) {

		Map<String, Object> map = new HashMap<>();
		map.put("status", false);
		map.put("message", "User Old password is testing ...");
		map.put("response", null);

		if (helperServices.isValidAndLenghtCheck(id, 30)) {
			User user = userServices.getUserByPublicID(id);
			if (resPassword != null && user != null) {

				if (passwordEncoder.matches(resPassword.getOldPass(), user.getPassword())) {
					map.put("message", "Old password is correct");
					map.put("status", true);
				} else {
					map.put("message", "Old password is not correct");
				}
			}

		}

		return ResponseEntity.ok(map);

	}
}
