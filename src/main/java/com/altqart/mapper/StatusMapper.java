package com.altqart.mapper;

import java.util.List;

import com.altqart.model.Status;
import com.altqart.resp.model.RespStatus;

public interface StatusMapper {

	public RespStatus mapRespStatus(Status status);

	public List<RespStatus> mapAllRespStatusOnly(List<Status> statuses);

}
