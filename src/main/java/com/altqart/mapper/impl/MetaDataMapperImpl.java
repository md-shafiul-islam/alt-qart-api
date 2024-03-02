package com.altqart.mapper.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.altqart.mapper.MetaDataMapper;
import com.altqart.model.MetaData;
import com.altqart.req.model.MetaDataReq;
import com.altqart.resp.model.RespMetaData;

@Service
public class MetaDataMapperImpl implements MetaDataMapper {

	@Override
	public Set<MetaData> mapProductMetaDatas(List<MetaDataReq> metaDatas) {
		if (metaDatas != null) {

			Set<MetaData> datas = new HashSet<>();

			for (MetaDataReq metaDataReq : metaDatas) {

				MetaData data = new MetaData();
				data.setContent(metaDataReq.getContent());
				data.setId(metaDataReq.getId());
				data.setName(metaDataReq.getName());

				datas.add(data);

			}

			return datas;
		}

		return null;
	}
	
	@Override
	public Set<RespMetaData> mapProductRespMetaDatas(Set<MetaData> metaDatas) {
		
		if(metaDatas != null) {
			Set<RespMetaData> respMetaDatas = new HashSet<>();
			
			for (MetaData metaData : metaDatas) {
				RespMetaData respMetaData = new RespMetaData();
				respMetaData.setContent(metaData.getContent());
				respMetaData.setName(metaData.getName());
				
				respMetaDatas.add(respMetaData);
			}
			
			return respMetaDatas;
		}
		
		return null;
	}

}
