package com.altqart.mapper;

import java.util.List;

import com.altqart.model.Receive;
import com.altqart.resp.model.RespReceive;

public interface ReceiveMapper {

	
	public List<RespReceive> mapAllRespReceive(List<Receive> receives);
	
	public RespReceive mapRespReceive(Receive receive);
}
