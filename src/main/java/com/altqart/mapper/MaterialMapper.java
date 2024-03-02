package com.altqart.mapper;

import java.util.List;
import java.util.Set;

import com.altqart.model.Material;
import com.altqart.req.model.MaterialReq;
import com.altqart.resp.model.RespMaterial;

public interface MaterialMapper {

	public List<RespMaterial> mapAllRespMaterial(List<Material> materials);

	public Material mapMaterial(MaterialReq materialReq);

	public RespMaterial mapRespMaterial(Material material);
	
	public RespMaterial mapRespMaterialOnly(Material material);

	public Set<RespMaterial> mapAllRespMaterialOnly(Set<Material> materials);

}
