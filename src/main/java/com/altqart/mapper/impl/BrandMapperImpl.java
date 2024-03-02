package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.BrandMapper;
import com.altqart.model.Brand;
import com.altqart.req.model.ReqBrand;
import com.altqart.resp.model.RespBrand;
import com.altqart.resp.model.SelectOption;

@Service
public class BrandMapperImpl implements BrandMapper {

	@Autowired
	private HelperServices helperServices;

	@Override
	public List<RespBrand> mapAllRespBrand(List<Brand> brands) {

		if (brands != null) {

			List<RespBrand> respBrands = new ArrayList<>();

			for (Brand brand : brands) {
				RespBrand respBrand = mapRespBrand(brand);
				if (respBrand != null) {
					respBrands.add(respBrand);
				}
			}

			return respBrands;
		}

		return null;
	}

	@Override
	public RespBrand mapRespBrand(Brand brand) {

		if (brand != null) {

			RespBrand respBrand = new RespBrand();
			respBrand.setDescription(brand.getDescription());
			respBrand.setName(brand.getName());

			respBrand.setId(brand.getPublicId());
			respBrand.setWeb(brand.getWebSite());
			respBrand.setKey(brand.getKey());
			respBrand.setLogoUrl(brand.getLogoUrl());
			respBrand.setTagLine(brand.getTagLine());
			return respBrand;
		}

		return null;
	}

	@Override
	public Brand mapBrand(ReqBrand reqBrand) {

		if (reqBrand != null) {
			Brand brand = new Brand();
			brand.setDescription(reqBrand.getDescription());
			brand.setName(reqBrand.getName());
			brand.setWebSite(reqBrand.getWeb());
			brand.setPublicId(helperServices.getGenPublicId());
			brand.setLogoUrl(reqBrand.getLogoUrl());
			brand.setTagLine(reqBrand.getTagLine());
			brand.setWebSite(reqBrand.getWeb());
			return brand;
		}

		return null;
	}

	@Override
	public List<SelectOption> mapAllRespBrandOption(List<Brand> brands) {

		if (brands != null) {

			List<SelectOption> options = new ArrayList<>();

			for (Brand brand : brands) {
				SelectOption option = new SelectOption();
				option.setValue(brand.getPublicId());
				option.setLabel(brand.getName());
				options.add(option);
			}

			return options;
		}
		return null;
	}

	@Override
	public List<Brand> mapBrandAll(List<ReqBrand> reqBrands) {

		if (reqBrands != null) {

			List<Brand> brands = new ArrayList<>();

			for (ReqBrand brandReq : reqBrands) {

				Brand brand = mapBrand(brandReq);

				if (brand != null) {
					brands.add(brand);
				}
			}

			return brands;

		}

		return null;
	}

	@Override
	public RespBrand mapBrandOnly(Brand brand) {

		if (brand != null) {
			RespBrand respBrand = new RespBrand();
			respBrand.setDescription(brand.getDescription());
			respBrand.setId(brand.getPublicId());
			respBrand.setKey(brand.getKey());
			respBrand.setLogoUrl(brand.getLogoUrl());
			respBrand.setName(brand.getName());
			respBrand.setTagLine(brand.getTagLine());
			respBrand.setWeb(brand.getWebSite());

			return respBrand;
		}
		return null;
	}

}
