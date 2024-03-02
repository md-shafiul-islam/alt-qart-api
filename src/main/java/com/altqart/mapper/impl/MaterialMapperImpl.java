package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.MaterialMapper;
import com.altqart.mapper.ProductMapper;
import com.altqart.model.Material;
import com.altqart.model.Product;
import com.altqart.req.model.MaterialReq;
import com.altqart.resp.model.RespMaterial;
import com.altqart.resp.model.RespProduct;

@Service
public class MaterialMapperImpl implements MaterialMapper {

	@Autowired
	private ProductMapper productMapper;

	@Override
	public List<RespMaterial> mapAllRespMaterial(List<Material> materials) {

		if (materials != null) {
			List<RespMaterial> respMaterials = new ArrayList<>();

			for (Material material : materials) {
				RespMaterial respMaterial = mapRespMaterialOnly(material);

				if (respMaterial != null) {
					respMaterials.add(respMaterial);
				}
			}

			return respMaterials;
		}

		return null;
	}

	@Override
	public Material mapMaterial(MaterialReq materialReq) {

		if (materialReq != null) {
			Material material = new Material();
			material.setDescription(materialReq.getDescription());
			material.setName(materialReq.getName());
			material.setSlug(materialReq.getSlug());
			return material;
		}

		return null;
	}

	@Override
	public RespMaterial mapRespMaterial(Material material) {

		if (material != null) {
			RespMaterial respMaterial = mapRespMaterialOnly(material);
			if (material.getProducts() != null) {
				respMaterial.setProducts(mapProducts(material.getProducts()));
			}
		}

		return null;
	}

	@Override
	public RespMaterial mapRespMaterialOnly(Material material) {

		if (material != null) {
			RespMaterial respMaterial = new RespMaterial();
			respMaterial.setId(material.getId());
			respMaterial.setDescription(material.getDescription());
			respMaterial.setName(material.getName());
			respMaterial.setSlug(material.getSlug());

			return respMaterial;
		}
		return null;
	}

	@Override
	public Set<RespMaterial> mapAllRespMaterialOnly(Set<Material> materials) {

		if (materials != null) {

			Set<RespMaterial> respMaterials = new HashSet<>();

			for (Material material : materials) {
				RespMaterial respMaterial = mapRespMaterialOnly(material);

				if (respMaterial != null) {
					respMaterials.add(respMaterial);
				}

			}
			return respMaterials;
		}

		return null;
	}

	private List<RespProduct> mapProducts(Set<Product> products) {
		List<RespProduct> respProducts = new ArrayList<>();

		if (products != null) {
			for (Product product : products) {

				RespProduct respProduct = productMapper.mapProductOnly(product);
				if (respProduct != null) {
					respProducts.add(respProduct);
				}
			}

			return respProducts;
		}

		return null;
	}

}
