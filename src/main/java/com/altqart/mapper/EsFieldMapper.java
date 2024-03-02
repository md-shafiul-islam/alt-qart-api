package com.altqart.mapper;

import com.altqart.model.Stakeholder;
import com.altqart.model.User;
import com.altqart.resp.model.RespEsField;

public interface EsFieldMapper {


	public RespEsField mapUserRespEsField(User user);

	public RespEsField mapCustomerRespEsField(Stakeholder customer);


}
