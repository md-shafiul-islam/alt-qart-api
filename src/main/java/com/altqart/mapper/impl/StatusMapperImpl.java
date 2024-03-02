package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.altqart.mapper.StatusMapper;
import com.altqart.model.Status;
import com.altqart.resp.model.RespStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatusMapperImpl implements StatusMapper {

	@Override
	public RespStatus mapRespStatus(Status status) {
		if (status != null) {

			RespStatus respStatus = new RespStatus();
			respStatus.setId(status.getId());
			respStatus.setName(status.getName());

			return respStatus;
		}
		return null;
	}

	@Override
	public List<RespStatus> mapAllRespStatusOnly(List<Status> statuses) {

		List<RespStatus> respStatuses = new ArrayList<>();

		if (statuses != null) {
			for (Status status : statuses) {
				RespStatus respStatus = mapRespStatusOnly(status);

				if (status != null) {
					respStatuses.add(respStatus);
				}
			}
		}

		return respStatuses;

	}

	public RespStatus mapRespStatusOnly(Status status) {

		if (status != null) {
			RespStatus respStatus = new RespStatus();
			respStatus.setId(status.getId());
			respStatus.setName(status.getName());
			return respStatus;
		}

		return null;
	}

}
