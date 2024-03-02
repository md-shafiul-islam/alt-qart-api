package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.PaidMapper;
import com.altqart.mapper.RoleMapper;
import com.altqart.mapper.SaleMapper;
import com.altqart.mapper.StoreMapper;
import com.altqart.mapper.UserMapper;
import com.altqart.model.Access;
import com.altqart.model.User;
import com.altqart.req.model.UserReq;
import com.altqart.resp.model.RespAccessUser;
import com.altqart.resp.model.RespEsUser;
import com.altqart.resp.model.RespUser;

@Service
public class UserMapperImpl implements UserMapper {

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private StoreMapper storeMapper;

	@Autowired
	private SaleMapper saleMapper;

	@Autowired
	private PaidMapper paidMapper;

	@Autowired
	private HelperServices helperServices;

	@Override
	public User getUser(UserReq userReq) {

		if (userReq != null) {
			User user = new User();
			user.setEmail(userReq.getEmail());
			user.setName(userReq.getName());
			user.setPhoneNo(userReq.getPhoneNo());
			user.setUsername(userReq.getUsername());
			user.setPublicId(helperServices.getGenPublicId());
			user.setCode(helperServices.getUserGenId());

			return user;
		}

		return null;
	}

	@Override
	public List<RespEsUser> mapAllRespEsUser(List<User> users) {

		if (users != null) {

			List<RespEsUser> esUsers = new ArrayList<>();

			for (User user : users) {
				RespEsUser esUser = mapRespEsUser(user);

				if (esUser != null) {
					esUsers.add(esUser);
				}
			}

			return esUsers;
		}

		return null;
	}

	@Override
	public RespEsUser mapRespEsUser(User user) {

		if (user != null) {
			RespEsUser respUser = new RespEsUser();
			respUser.setCode(user.getCode());
			respUser.setEmail(user.getEmail());
			respUser.setName(user.getName());
			respUser.setPhoneNo(user.getPhoneNo());
			respUser.setPublicId(user.getPublicId());

			return respUser;
		}

		return null;
	}

	@Override
	public RespUser mapRespUserOnly(User user) {

		if (user != null) {
			RespUser respUser = new RespUser();
			respUser.setAccountNonExpired(user.getAccountNonExpired());
			respUser.setCode(user.getCode());
			respUser.setDate(user.getDate());
			respUser.setEmail(user.getEmail());
			respUser.setEnabled(user.getEnabled());
			respUser.setId(user.getPublicId());
			respUser.setLocked(user.getLocked());
			respUser.setName(user.getName());
			respUser.setPhoneNo(user.getPhoneNo());
			respUser.setUdate(user.getUdate());

			return respUser;
		}

		return null;
	}

	@Override
	public List<RespUser> mapAllRespUser(List<User> users) {
		if (users != null) {

			List<RespUser> respUsers = new ArrayList<>();

			for (User user : users) {
				RespUser respUser = mapRespUserOnly(user);

				if (respUser != null) {
					respUsers.add(respUser);
				}
			}

			return respUsers;
		}

		return null;
	}

	@Override
	public RespUser mapRespUserDetails(User user) {
		if (user != null) {

			RespUser respUser = mapRespUserOnly(user);

			if (respUser != null) {

				respUser.setRole(roleMapper.mapRoleOnly(user.getRole()));
				respUser.setStore(storeMapper.mapRespStoreOnly(user.getStore()));
			}

			return respUser;
		}
		return null;
	}

	@Override
	public List<RespAccessUser> mapAllUserAccess(List<Access> accesses) {
		// TODO Auto-generated method stub
		return null;
	}

}
