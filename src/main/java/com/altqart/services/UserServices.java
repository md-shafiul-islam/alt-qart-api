package com.altqart.services;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.altqart.model.Credential;
import com.altqart.model.User;
import com.altqart.req.model.UserReq;
import com.altqart.resp.model.RespEsUser;
import com.altqart.resp.model.RespUser;

public interface UserServices extends UserDetailsService {

	public User getUserById(int id);

	public User getCurrentUser();

	public List<User> getAllUser();

	public boolean saveUser(User user);

	public long getCount();

	public User getUserByUsername(String name);

	public boolean updatePassword(Credential credential);

	public User getUserByPublicID(String userId);

	public User getUserByUserNameAndPass(String name, String password);

	public String getUnicId();

	public User getUserByPhoneNo(String phoneNo);

	public User getUserByEmail(String email);

	public List<RespEsUser> getAllRespEsUserOnly();

	public List<RespUser> getAllRespUserOnly();

	public RespUser getUserDetailsByPublicID(String id);

	public boolean updateUserLocked(int locked, User user);

	public boolean updateUserEnabled(int enabled, User user);

	public User getOnlyUserByPublicID(String userId);

	public void signUpUser(UserReq user, Map<String, Object> map);

}
