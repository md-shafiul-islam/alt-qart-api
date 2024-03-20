package com.altqart.resp.model;

import java.util.List;

import com.altqart.model.Parcel;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespParcelProvider {

	private String id;

	private List<RespParcel> parcels;

	private String baseUrl;

	private String name;

	private String key;

	private String idOrKey; // String Or Id

	private String secret;

	private String email;

	private String password;

	private String grantType; // password/token

	private String webhookSecret;

	private String tokenLink = "/aladdin/api/v1/issue-token";

	private String refreshToken;

	private String oredreLink;

	private String bulkOredreLink;

	private String shortLink;

	private String storeLink = "/aladdin/api/v1/stores";

	private String priceingLink;
}
