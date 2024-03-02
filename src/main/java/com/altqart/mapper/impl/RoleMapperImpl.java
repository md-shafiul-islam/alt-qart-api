package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.RoleMapper;
import com.altqart.mapper.UserMapper;
import com.altqart.model.Access;
import com.altqart.model.Role;
import com.altqart.req.model.RoleReq;
import com.altqart.resp.model.RespAccess;
import com.altqart.resp.model.RespRole;

@Service
public class RoleMapperImpl implements RoleMapper {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private HelperServices helperServices;

	@Override
	public List<RespRole> mapAllRespRoleOnly(List<Role> roles) {

		List<RespRole> respRoles = new ArrayList<>();

		if (roles != null) {

			for (Role role : roles) {
				RespRole respRole = mapRoleOnly(role);
				if (respRole != null) {
					respRoles.add(respRole);
				}
			}

			return respRoles;
		}
		return null;
	}

	@Override
	public RespRole mapRoleOnly(Role role) {

		if (role != null) {

			RespRole respRole = new RespRole();

			respRole.setAuthStatus(role.getAuthStatus());
			respRole.setDate(role.getDate());
			respRole.setDateGroupe(role.getDateGroupe());
			respRole.setDescription(role.getDescription());
			respRole.setGenId(role.getGenId());
			respRole.setId(role.getPublicId());
			respRole.setName(role.getName());

			return respRole;
		}

		return null;
	}

	@Override
	public RespRole mapRoleDetails(Role role) {

		if (role != null) {

			RespRole respRole = mapRoleOnly(role);

			if (respRole != null) {
				respRole.setAccesses(mapAllRespAccess(role.getAccesses()));
				respRole.setUsers(userMapper.mapAllRespEsUser(role.getUsers()));

				return respRole;
			}
		}

		return null;
	}

	@Override
	public List<RespAccess> mapAllRespAccess(List<Access> accesses) {

		if (accesses != null) {

			List<RespAccess> respAccesses = new ArrayList<>();

			for (Access access : accesses) {
				RespAccess respAccess = mapRespAccessOnly(access);

				if (respAccess != null) {
					respAccesses.add(respAccess);
				}
			}

			return respAccesses;
		}

		return null;
	}

	@Override
	public RespAccess mapRespAccessOnly(Access access) {

		if (access != null) {
			RespAccess respAccess = new RespAccess();

			respAccess.setAdd(access.isAdd());
			respAccess.setAll(access.isAll());
			respAccess.setApprove(access.isApprove());
			respAccess.setDescription(access.getDescription());
			respAccess.setEdit(access.isEdit());
			respAccess.setId(access.getPublicId());
			respAccess.setName(access.getName());
			respAccess.setNoAccess(access.isNoAccess());
			respAccess.setUpdateApproval(access.isUpdateApproval());
			respAccess.setView(access.isView());

			return respAccess;
		}

		return null;
	}

	@Override
	public Role mapRole(RoleReq roleReq) {

		if (roleReq != null) {
			Role role = new Role();

			role.setAuthStatus(roleReq.getAuthStatus());
			role.setDescription(roleReq.getDescription());
			role.setGenId("AR_" + helperServices.getGenaretedIDUsingDateTime());
			role.setName(roleReq.getName());
			role.setPublicId(helperServices.getGenPublicId());

			if (roleReq.getDate() != null) {
				role.setDate(roleReq.getDate());
				role.setDateGroupe(roleReq.getDate());

			} else {
				Date date = new Date();
				role.setDate(date);
				role.setDateGroupe(date);
			}

			return role;
		}

		return null;
	}
}
