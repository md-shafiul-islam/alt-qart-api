package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.altqart.mapper.SpecificationMapper;
import com.altqart.model.SpecKey;
import com.altqart.model.Specification;
import com.altqart.resp.model.RespSpecKey;
import com.altqart.resp.model.RespSpecification;

@Service
public class SpecificationMapperImpl implements SpecificationMapper {

	@Override
	public List<RespSpecification> mapRespSpecifications(List<Specification> specifications) {

		if (specifications != null) {

			List<RespSpecification> respSpecifications = new ArrayList<>();

			for (Specification specification : specifications) {
				RespSpecification respSpecification = mapRespSpecificationOnly(specification);

				if (specification != null) {
					respSpecifications.add(respSpecification);
				}

			}

			return respSpecifications;
		}

		return null;
	}

	@Override
	public RespSpecification mapRespSpecificationOnly(Specification specification) {

		if (specification != null) {
			RespSpecification respSpecification = new RespSpecification();
			respSpecification.setDescription(specification.getDescription());
			respSpecification.setFeature(specification.isFeature());
			respSpecification.setValue(specification.getValue());
			respSpecification.setKey(mapRespKey(specification.getKey()));

			return respSpecification;
		}

		return null;
	}

	public RespSpecKey mapRespKey(SpecKey key) {

		if (key != null) {
			RespSpecKey respSpecKey = new RespSpecKey();
			respSpecKey.setId(key.getId());
			respSpecKey.setName(key.getName());
			respSpecKey.setType(key.getType());
			respSpecKey.setValue(key.getValue());

			return respSpecKey;

		}
		return null;
	}

}
