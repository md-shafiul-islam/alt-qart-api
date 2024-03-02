package com.altqart.services;

import java.util.List;

import com.altqart.model.StakeholderType;
import com.altqart.resp.model.RespStakeholderType;

public interface StakeholderTypeServices {

	public StakeholderType getStakeholderTypeByKey(String key);

	public List<RespStakeholderType> getAllStakeholderType();

}
