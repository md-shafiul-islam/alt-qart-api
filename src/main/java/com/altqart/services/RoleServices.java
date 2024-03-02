package com.altqart.services;

import java.util.List;
import java.util.Map;

import com.altqart.req.model.RoleReq;
import com.altqart.resp.model.RespRole;

public interface RoleServices {

	public List<RespRole> getAllRespAccessRole();

	public void addAccessRole(RoleReq roleReq, Map<String, Object> map);

	public RespRole getRespAccessRoleById(String id);

}
