package com.altqart.helper.services;

import com.altqart.model.Access;
import com.altqart.model.User;

public interface HelperAuthenticationServices {

	public User getCurrentUser();

	public Access getAccessByMenuName(String string);

	public boolean isOwn(String id);

}
