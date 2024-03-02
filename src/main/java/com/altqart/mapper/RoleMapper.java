package com.altqart.mapper;

import java.util.List;

import com.altqart.model.Access;
import com.altqart.model.Role;
import com.altqart.req.model.RoleReq;
import com.altqart.resp.model.RespAccess;
import com.altqart.resp.model.RespRole;

public interface RoleMapper {

	public List<RespRole> mapAllRespRoleOnly(List<Role> roles);

	public Role mapRole(RoleReq roleReq);

	public RespRole mapRoleDetails(Role role);
	
	public RespRole mapRoleOnly(Role role);
	
	public List<RespAccess> mapAllRespAccess(List<Access> accesses);
	
	public RespAccess mapRespAccessOnly(Access access);

}
