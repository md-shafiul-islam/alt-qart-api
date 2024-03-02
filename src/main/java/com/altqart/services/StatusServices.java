package com.altqart.services;
import java.util.List;

import com.altqart.model.Status;
import com.altqart.resp.model.RespStatus;

public interface StatusServices {

	public Status getStatusById(int id);
	
	public List<Status> getAllStatus();

	public RespStatus getRespStatusById(int id);

	public List<RespStatus> getAllRespStatus();
	
}
