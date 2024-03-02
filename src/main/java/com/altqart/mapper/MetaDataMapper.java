package com.altqart.mapper;

import java.util.List;
import java.util.Set;

import com.altqart.model.MetaData;
import com.altqart.req.model.MetaDataReq;
import com.altqart.resp.model.RespMetaData;

public interface MetaDataMapper {

	public Set<MetaData> mapProductMetaDatas(List<MetaDataReq> metaDatas);

	public Set<RespMetaData> mapProductRespMetaDatas(Set<MetaData> metaDatas);

}
