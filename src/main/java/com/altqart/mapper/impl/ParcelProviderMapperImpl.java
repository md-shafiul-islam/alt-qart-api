package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.ParcelMapper;
import com.altqart.mapper.ParcelProviderMapper;
import com.altqart.model.ParcelProvider;
import com.altqart.req.model.ParcelProviderReq;
import com.altqart.resp.model.RespMinParcelProvider;
import com.altqart.resp.model.RespParcelProvider;

@Service
public class ParcelProviderMapperImpl implements ParcelProviderMapper {

	@Autowired
	private ParcelMapper parcelMapper;

	@Override
	public ParcelProvider mapParcelProvider(ParcelProviderReq parcelProviderReq) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RespParcelProvider mapRespParcelProvider(ParcelProvider provider) {

		RespParcelProvider parcelProvider = mapRespParcelProviderOnly(provider);

		if (parcelProvider != null) {
			parcelProvider.setParcels(parcelMapper.mapAllParcel(provider.getParcels()));
		}
		return parcelProvider;
	}

	@Override
	public RespParcelProvider mapRespParcelProviderOnly(ParcelProvider parcelProvider) {

		if (parcelProvider != null) {
			RespParcelProvider provider = new RespParcelProvider();
			provider.setBaseUrl(parcelProvider.getBaseUrl());
			provider.setBulkOredreLink(parcelProvider.getBulkOredreLink());
			provider.setEmail(parcelProvider.getEmail());
			provider.setGrantType(parcelProvider.getGrantType());
			provider.setId(parcelProvider.getPublicId());
			provider.setIdOrKey(parcelProvider.getIdOrKey());
			provider.setKey(parcelProvider.getKey());
			provider.setName(parcelProvider.getName());
			provider.setOredreLink(parcelProvider.getOredreLink());
			provider.setPassword(parcelProvider.getPassword());
			provider.setPriceingLink(parcelProvider.getPriceingLink());
			provider.setRefreshToken(parcelProvider.getRefreshToken());
			provider.setSecret(parcelProvider.getSecret());
			provider.setShortLink(parcelProvider.getShortLink());
			provider.setStoreLink(parcelProvider.getStoreLink());
			provider.setWebhookSecret(parcelProvider.getWebHookSecret());

			return provider;
		}
		return null;
	}

	@Override
	public List<ParcelProvider> mapAllRespParcelProvider(List<ParcelProvider> providers) {
		if (providers != null) {

			List<ParcelProvider> parcelProviders = new ArrayList<>();

			for (ParcelProvider parcelProvider : parcelProviders) {

				RespParcelProvider respParcelProvider = mapRespParcelProviderOnly(parcelProvider);

				if (respParcelProvider != null) {
					parcelProviders.add(parcelProvider);
				}
			}

			return parcelProviders;
		}
		return null;
	}

	@Override
	public RespMinParcelProvider mapRespMinParcelProviderOnly(ParcelProvider parcelProvider) {

		if (parcelProvider != null) {

			RespMinParcelProvider minParcelProvider = new RespMinParcelProvider();
			minParcelProvider.setId(parcelProvider.getPublicId());
			minParcelProvider.setKey(parcelProvider.getKey());
			minParcelProvider.setName(parcelProvider.getName());

			return minParcelProvider;
		}

		return null;
	}

}
