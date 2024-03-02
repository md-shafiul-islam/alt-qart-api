package com.altqart.helper.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.model.Access;
import com.altqart.model.User;
import com.altqart.services.UserServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HelperAuthenticationServicesImpl implements HelperAuthenticationServices {

	@Autowired
	private UserServices userServices;

	@Autowired
	private HelperServices helperServices;

	@Override
	public User getCurrentUser() {

//		System.out.println("Helper Get Current User !!");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		System.out.println("authentication.getPrincipal().toString()" + authentication.getPrincipal().toString());
		if (authentication != null) {

			if (authentication.getName() != null) {

				if (authentication != null && authentication.getCredentials() != null) {

					return userServices.getUserByUserNameAndPass(authentication.getName(),
							authentication.getCredentials().toString());
				}

				return userServices.getUserByUsername(authentication.getName());
			}

			if (authentication.getPrincipal() != null) {
				return userServices.getUserByUsername(authentication.getPrincipal().toString());
			}

		}

		return null;
	}

	@Override
	public Access getAccessByMenuName(String name) {
		Access userAccess = null;
		User user = getCurrentUser();

		if (user != null) {
			user = userServices.getUserById(user.getId());

			if (user.getRole() != null) {

				if (user.getRole().getAccesses() != null) {

					for (Access access : user.getRole().getAccesses()) {
						if (helperServices.isEqual(access.getAccessType().getValue(), name)) {
							userAccess = access;
						}
					}
				}
			}
		}

		return userAccess;
	}

	@Override
	public boolean isOwn(String id) {
		User user = getCurrentUser();

		if (helperServices.isEqual(id, user.getPublicId())) {
			return true;
		}
		log.info("IsOwn " + false);

		return false;
	}

}
