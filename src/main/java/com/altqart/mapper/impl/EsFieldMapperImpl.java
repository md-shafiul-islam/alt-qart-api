package com.altqart.mapper.impl;

import org.springframework.stereotype.Service;

import com.altqart.mapper.EsFieldMapper;
import com.altqart.model.Stakeholder;
import com.altqart.model.User;
import com.altqart.resp.model.RespEsField;

@Service
public class EsFieldMapperImpl implements EsFieldMapper {

	@Override
	public RespEsField mapUserRespEsField(User user) {

		if (user != null) {

			RespEsField esField = new RespEsField();
			esField.setId(user.getPublicId());
			esField.setName(user.getName());

			return esField;
		}
		return null;
	}

	@Override
	public RespEsField mapCustomerRespEsField(Stakeholder customer) {

		if (customer != null) {

			RespEsField esField = new RespEsField();

			if (customer != null) {
				esField.setName(customer.getName());
				esField.setId(customer.getPublicId());
			}

			return esField;
		}

		return null;
	}


}
