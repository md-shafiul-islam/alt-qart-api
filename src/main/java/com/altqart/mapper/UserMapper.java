package com.altqart.mapper;

import java.util.List;

import com.altqart.model.Access;
import com.altqart.model.User;
import com.altqart.req.model.UserReq;
import com.altqart.resp.model.RespAccessUser;
import com.altqart.resp.model.RespEsUser;
import com.altqart.resp.model.RespUser;

public interface UserMapper {

	public User getUser(UserReq user);

	public List<RespEsUser> mapAllRespEsUser(List<User> users);

	public RespEsUser mapRespEsUser(User user);

	public List<RespUser> mapAllRespUser(List<User> users);

	public RespUser mapRespUserOnly(User user);

	public RespUser mapRespUserDetails(User user);

	public List<RespAccessUser> mapAllUserAccess(List<Access> accesses);

}
